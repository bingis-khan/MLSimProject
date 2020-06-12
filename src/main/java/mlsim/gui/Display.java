package mlsim.gui;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
/**
 * Class contain a simulation window
 * 
 */
public class Display {
	
	private JFrame frame;
	private Canvas canvas;
	
	private final String title;
	private final int width, height;
	
	public Display(String title, int width, int height, Gui gui) {
		this.title = title;
		this.width = width;
		this.height = height;
		
		createDisplay(gui);
	}
	
	private void createDisplay(final Gui gui) {
		frame = new JFrame(title);
		frame.setSize(width,  height);
		
		// Adds a custom window listener to only exit the gui.
		frame.addWindowListener(new WindowListener() {

			@Override
			public void windowOpened(WindowEvent e) {}

			@Override
			public void windowClosing(WindowEvent e) {
				gui.stop();
			}

			@Override
			public void windowClosed(WindowEvent e) {}

			@Override
			public void windowIconified(WindowEvent e) {}

			@Override
			public void windowDeiconified(WindowEvent e) {}

			@Override
			public void windowActivated(WindowEvent e) {}

			@Override
			public void windowDeactivated(WindowEvent e) {}
			
		});
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	
		canvas = new Canvas();
		canvas.setPreferredSize(new Dimension(width, height));
		canvas.setMaximumSize(new Dimension(width, height));
		canvas.setMinimumSize(new Dimension(width, height));
		canvas.setFocusable(false);
		
		frame.add(canvas);
		frame.pack();
	}
	/**
	 * Method returns size canvas
	 * 
	 */
	public Canvas getCanvas() {
		return canvas;
	}
	/**
	 * @return value of frame 
	 */
	public JFrame getFrame() {
		return frame;
	}
}
