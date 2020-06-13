package mlsim.gui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
/**
 * Save information what keys the user press
 *
 */
public class KeyManager implements KeyListener {

	private boolean[] keys, justPressed, cantPress;
	
	public KeyManager() {
		keys = new boolean[256];
		justPressed = new boolean[keys.length];
		cantPress = new boolean[keys.length];
	}
	/**
	 * give a value of key which the user presses
	 */
	public void tick() {
		for(int i = 0; i < keys.length; i++) {
			if(cantPress[i] && !keys[i]) {
				cantPress[i] = false;
			} else if(justPressed[i]) {
				cantPress[i] = true;
				justPressed[i] = false;
			}
			if(!cantPress[i] && keys[i])
				justPressed[i] = true;
		}
	}
	/**
	 * @return a string of key that the user pressed
	 */
	public boolean keyJustPressed(int key) {
		return justPressed[key];
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() < 0 || e.getKeyCode() >= keys.length)
			return;
		keys[e.getKeyCode()] = true;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() < 0 || e.getKeyCode() >= keys.length)
			return;
		keys[e.getKeyCode()] = false;
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}

}
