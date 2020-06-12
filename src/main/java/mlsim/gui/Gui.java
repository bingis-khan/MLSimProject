package mlsim.gui;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

import mlsim.simulation.Entity;
import mlsim.simulation.Simulation;
import mlsim.simulation.SimulationState;

public class Gui {

	private Display display;
	private final int windowWidth, windowHeight;
	private final String title;
	private final Simulation simulation;

	private boolean running = true;

	private BufferStrategy bs;
	private Graphics g;

	// Input
	private KeyManager keyManager;
	
	private int ticksPerSecond;
	
	// Camera
	int x = 0, y = 0, width, height;
	
	private boolean paused = false;
	
	private final BufferedImage agentImage, foodImage, bgImage;

	
	public Gui(String title, int windowWidth, int windowHeight, Simulation simulation) {
		this.windowWidth = windowWidth;
		this.windowHeight = windowHeight;
		this.title = title;
		this.simulation = simulation;
		
		width = simulation.getWidth();
		height = simulation.getHeight();
		
		keyManager = new KeyManager();
		
		try {
			bgImage = ImageIO.read(new File("src/main/resources/t³o.jpg"));
			agentImage = ImageIO.read(new File("src/main/resources/spurdo.jpg"));
			foodImage = ImageIO.read(new File("src/main/resources/jab³ko.jpg"));
		} catch(IOException e) {
			throw new RuntimeException("Bruh, images can't load. Shieet." + e.getMessage());
		}
	}

	private void init() {
		display = new Display(title, windowWidth, windowHeight, this);
		display.getFrame().addKeyListener(keyManager);
	}

	private void updateKeys() {
		keyManager.tick();
	}

	private void tick() {
		if (keyManager.keyJustPressed(KeyEvent.VK_SPACE)) {
			paused = !paused;
		}
	}
	
	private void move(int moveX, int moveY) {
		if (x + moveX >= 0 && y + moveY >= 0 &&
				width + x + moveX < simulation.getWidth() &&
				height + y + moveY < simulation.getHeight()) {
			x += moveX;
			y += moveX;
		}
	}

	public void render() {
		bs = display.getCanvas().getBufferStrategy();
		if (bs == null) {
			display.getCanvas().createBufferStrategy(3);
			return;
		}
		g = bs.getDrawGraphics();

		
		render(g, x, y, width, height, simulation.getSimulationState());

		// End drawin'
		bs.show();
		g.dispose();
	}
	
	/* PAT (poprawione) */

	void render(Graphics g, int x, int y, int width, int height, SimulationState s) {
		// Draw background
		g.drawImage(bgImage, 0, 0, windowWidth, windowHeight, null);
		
		int cellWidth = windowWidth / width;
		int cellHeight = windowHeight / height;

		for (Entity agent : s.agents()) {
			drawEntity(g, agent.getX() - x, agent.getY() - y, cellWidth, cellHeight, agentImage);
		}
		
		for (Entity food : s.food()) {
			drawEntity(g, food.getX() - x, food.getY() - y, cellWidth, cellHeight, foodImage);
		}

		g.drawString(ticksPerSecond+"", windowWidth - 15, 10);
	}

	void drawEntity(Graphics g, int x, int y, int cellWidth, int cellHeight, BufferedImage entityimage) {
		g.drawImage(entityimage, x * cellWidth, y * cellHeight, cellWidth, cellHeight, null);
	}
	
	/* END PAT */

	public void run() {
		init();

		int fps = 60;
		int ticksPerStep = 60;
		double timePerTick = 1_000_000_000 / fps;
		double delta = 0;
		long now;
		long lastTime = System.nanoTime();
		long timer = 0;
		int ticks = 0;
		int stepTicks = 0;

		while (running) {
			now = System.nanoTime();
			delta += (now - lastTime) / timePerTick;
			timer += now - lastTime;
			lastTime = now;
			if (delta >= 1) {
				updateKeys(); // Check input
				tick();
				ticks++;
				delta--;
				stepTicks++;

				if (stepTicks == ticksPerStep) { // Step through the simulation every few ticks.
					stepTicks = 0;

					if (!paused) simulation.step();
				}
			}
			if (timer >= 1000000000) {
				ticksPerSecond = ticks;
				ticks = 0;
				timer = 0;
			}
			render();
		}
	}

	public void stop() {
		running = false;
	}
}
