package mlsim.gui;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import mlsim.simulation.Entity;
import mlsim.simulation.Simulation;
import mlsim.simulation.SimulationState;
/**
 * class has the environment of the simulation
 * 
 * @author bingis_khan 
 *
 */
public class Gui {

	private Display display;
	private final int windowWidth, windowHeight;
	private final String title;
	private final Simulation simulation;

	private boolean running = true;

	private BufferStrategy bs;
	private Graphics g;

	private int ticksPerSecond = 0;

	// Temp
	private int os = 0;

	// Input
	private KeyManager keyManager;
	
	private final BufferedImage agentImage, foodImage, bgImage;

	
	public Gui(String title, int windowWidth, int windowHeight, Simulation simulation) {
		this.windowWidth = windowWidth;
		this.windowHeight = windowHeight;
		this.title = title;
		this.simulation = simulation;
		keyManager = new KeyManager();
		
		try {
			bgImage = ImageIO.read(getClass().getResource("t³o.jpg"));
			agentImage = ImageIO.read(getClass().getResource("spinacz.jpg"));
			foodImage = ImageIO.read(getClass().getResource("jablko.jpg"));
		} catch(IOException e) {
			throw new RuntimeException("Bruh, images can't load. Shieet.");
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
		if (keyManager.keyJustPressed(KeyEvent.VK_O))
			os++;
	}
/**
 * Draw on the canvas.
 */
	public void render() {
		bs = display.getCanvas().getBufferStrategy();
		if (bs == null) {
			display.getCanvas().createBufferStrategy(3);
			return;
		}
		g = bs.getDrawGraphics();

		// Drawin'

		// Current frame.
		BufferedImage frame = render(0, 0, simulation.getWidth(), simulation.getHeight(),
				simulation.getSimulationState());
		// Draws it on the canvas.
		g.drawImage(frame, 0, 0, windowWidth, windowHeight, null);

		// End drawin'
		bs.show();
		g.dispose();
	}
	
	/* PAT (poprawione) */
	/**
	 * 
	 * Draw agents and food on the board 
	 * 
	 */
	BufferedImage render(int x, int y, int width, int height, SimulationState s) {
		BufferedImage background = new BufferedImage(bgImage.getWidth(), bgImage.getHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics g = background.getGraphics();

		int cellWidth = bgImage.getWidth() / width;
		int cellHeight = bgImage.getHeight() / height;

		for (Entity agent : s.agents()) {
			drawEntity(g, x - agent.getX(), y - agent.getY(), cellWidth, cellHeight, agentImage);
		}
		for (Entity food : s.food()) {
			drawEntity(g, x - food.getX(), y - food.getY(), cellWidth, cellHeight, foodImage);
		}
		g.dispose();

		return background;

	}
	/**
	 * Method of drawing agents on the board
	 * @param g, x, y,,cellWidth, cellHeight, entityimage
	 */

	void drawEntity(Graphics g, int x, int y, int cellWidth, int cellHeight, BufferedImage entityimage) {
		g.drawImage(entityimage, x * cellWidth, y * cellHeight, cellWidth, cellHeight, null);
	}
	
	/* END PAT */
	/**
	 * main method which shows environment of simulation
	 */
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

					simulation.step();
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
