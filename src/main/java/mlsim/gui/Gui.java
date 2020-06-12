package mlsim.gui;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;


import mlsim.simulation.Simulation;

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
	
	public Gui(String title, int windowWidth, int windowHeight, Simulation simulation) {
		this.windowWidth = windowWidth;
		this.windowHeight = windowHeight;
		this.title = title;
		this.simulation = simulation;
		keyManager = new KeyManager();
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
	
	public void render() {
		bs = display.getCanvas().getBufferStrategy();
		if(bs == null) {
			display.getCanvas().createBufferStrategy(3);
			return;
		}
		g = bs.getDrawGraphics();
		
		// Drawin'
		
		// Current frame.
		BufferedImage frame = render(0, 0, simulation.getWidth(), simulation.getHeight());
		// Draws it on the canvas.
		g.drawImage(frame, 0, 0, windowWidth, windowHeight, null);
		
		// End drawin'
		bs.show();
		g.dispose();
	}
	
	private BufferedImage render(int x, int y, int width, int height) {
		// Temporary
		BufferedImage image = new BufferedImage(windowWidth, windowHeight, BufferedImage.TYPE_INT_RGB);
		Graphics g = image.getGraphics();
		
		g.drawString("steps: "  + simulation.getSteps() + "; fps: " + ticksPerSecond + "; o pressed: " + os, windowWidth/2, windowHeight/2);
		
		g.dispose();
		
		return image;
	}
	
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
		
		while(running) {
			now = System.nanoTime();
			delta += (now - lastTime) / timePerTick;
			timer += now - lastTime;
			lastTime = now;
			if(delta >= 1) {
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
			if(timer >= 1000000000) {
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
