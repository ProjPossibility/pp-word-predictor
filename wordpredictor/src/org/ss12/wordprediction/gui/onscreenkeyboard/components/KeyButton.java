package org.ss12.wordprediction.gui.onscreenkeyboard.components;

import javax.swing.JButton;

public class KeyButton extends JButton {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2960941162106099259L;
	String buttonText,shiftButtonText;
	int keycode;
	boolean shift=false;
	public KeyButton(String text, String shiftText, int keycode) {
		super(text);
		this.buttonText=text;
		this.shiftButtonText=shiftText;
		this.keycode = keycode;
	}
	public void setShift(boolean shifted){
		shift = shifted;
		this.setText(shift ? shiftButtonText : buttonText);
	}
	public int getKeyCode(){
		return keycode;
	}
}
