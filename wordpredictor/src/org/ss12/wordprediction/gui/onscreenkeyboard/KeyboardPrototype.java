package org.ss12.wordprediction.gui.onscreenkeyboard;

import java.awt.AWTException;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class KeyboardPrototype extends JFrame implements ActionListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JButton button;
	Clipboard clip;
	JTextField text;
	Robot myRobot;
	private boolean shift;

	public KeyboardPrototype(Robot robot) {
		myRobot = robot;
		shift=false;
		JPanel main = new JPanel();

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				cleanup();
				System.exit(0);
			}
		});
		main.setLayout(new BoxLayout(main, BoxLayout.PAGE_AXIS));
		text = new JTextField("Hello");
		main.add(text);
		String[] numbers = {"`","1","2","3","4","5","6","7","8","9","0","-","+","Backspace"};
		main.add(rowOfKeys(numbers));
		String[] first = {"Tab","q","w","e","r","t","y","u","i","o","p","[","]","\\"};
		main.add(rowOfKeys(first));
		String[] second = {"Caps Lock","a","s","d","f","g","h","i","j","k","l",";","'","Enter"};
		main.add(rowOfKeys(second));
		String[] third = {"Shift","z","x","c","v","b","n","m",",",".","/","Shift"};
		main.add(rowOfKeys(third));
		String[] fourth = {"Ctrl","Alt","Space","Alt","Ctrl"};
		main.add(rowOfKeys(fourth));
		this.getContentPane().add(main);
	}
	private void getText() {
		myRobot.keyPress(KeyEvent.VK_SHIFT);
		myRobot.keyPress(KeyEvent.VK_UP);
		myRobot.keyRelease(KeyEvent.VK_UP);
		myRobot.keyRelease(KeyEvent.VK_SHIFT);
		myRobot.keyPress(KeyEvent.VK_CONTROL);
		myRobot.keyPress(KeyEvent.VK_C);
		myRobot.keyRelease(KeyEvent.VK_C);
		myRobot.keyRelease(KeyEvent.VK_CONTROL);
		myRobot.keyPress(KeyEvent.VK_DOWN);
		myRobot.keyRelease(KeyEvent.VK_DOWN);
		try {
			Thread.sleep(100);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Toolkit tk = Toolkit.getDefaultToolkit();
		clip = tk.getSystemClipboard();
		Transferable contents = clip.getContents(null);
		try {
			String s = (String)contents.getTransferData(DataFlavor.stringFlavor);
			text.setText(s);
		} catch (UnsupportedFlavorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private Component rowOfKeys(String[] keys) {
		Box b = new Box(BoxLayout.LINE_AXIS);
		for(int i=0;i<keys.length;i++){
			button = new JButton(keys[i]);
			button.addActionListener(this);
			if(button.getText()=="Space")
				button.setPreferredSize(new Dimension(200,10));
			b.add(button);	
		}
		return b;
	}
	public void cleanup(){
		//predictor.cleanup();
	}


	/**
	 * @param args
	 */
	public static void main(String[] args) throws AWTException{
		KeyboardPrototype gl = new KeyboardPrototype(new Robot());
		gl.setDefaultCloseOperation(EXIT_ON_CLOSE);
		gl.setAlwaysOnTop(true);
		gl.setSize(650, 200);
		gl.setFocusableWindowState(false);

		gl.setVisible(true);
	}
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		if(arg0.getSource().getClass().isInstance(button)){
			String key = ((JButton)arg0.getSource()).getText();
			if(key.equals("Enter")) press(KeyEvent.VK_ENTER);
			else if(key.equals("Space")) press(KeyEvent.VK_SPACE);
			else if(key.equals("Ctrl")) press(KeyEvent.VK_CONTROL);
			else if(key.equals("Caps Lock")) press(KeyEvent.VK_CAPS_LOCK);
			else if(key.equals("Shift")){
				if(shift)
					myRobot.keyRelease(KeyEvent.VK_SHIFT);
				else
					myRobot.keyPress(KeyEvent.VK_SHIFT);
				shift = !shift;
			}
			else if(key.equals("Alt")) press(KeyEvent.VK_ALT);
			else if(key.equals("Tab")) press(KeyEvent.VK_TAB);
			else if(key.equals("Backspace")) press(KeyEvent.VK_BACK_SPACE);
			else{
				typeString(key);
			}
			//myRobot.delay(100);
			getText();
//			button.setText(""+(char)(button.getText().charAt(0)+1));
		}
	}
	private void press(int key) {
		myRobot.keyPress(key);
		//might need to add a delay here?  Works on Ubuntu/XP
		myRobot.keyRelease(key);
	}
	private void typeString(String str) {
		str = str.toLowerCase();
		for(int i=0;i<str.length();i++){
			if(shift){
				myRobot.keyPress(KeyEvent.VK_SHIFT);
			}
			int key = KeyEvent.VK_A+str.charAt(i)-'a';
			press(key);
			myRobot.keyRelease(KeyEvent.VK_SHIFT);
			shift=false;
		}
	}

}