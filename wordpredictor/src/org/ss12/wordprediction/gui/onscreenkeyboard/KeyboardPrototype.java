package org.ss12.wordprediction.gui.onscreenkeyboard;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
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

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.ss12.wordprediction.WordPredictor;
import org.ss12.wordprediction.model.PredictionModel;

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
	JButton[] wordButtons;
	PredictionModel predictor;

	public KeyboardPrototype(Robot robot) {
		predictor = new WordPredictor();
		myRobot = robot;
		shift=false;
		JPanel main = new JPanel();

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				cleanup();
				System.exit(0);
			}
		});
		main.setLayout(new BorderLayout());
		text = new JTextField("");
		text.setFont(text.getFont().deriveFont(20f));
		main.add(text, BorderLayout.NORTH);
		
		JPanel keyboard = new JPanel();
		
		keyboard.setLayout(new BoxLayout(keyboard, BoxLayout.PAGE_AXIS));
		//text = new JTextField("     1. Experience                    2. Expected                    3. Expect                    4. Experiments                    5. Experiment");

		String[] words={" "," "," "," "," "};
		JPanel wordRow = rowOfKeys(words);
		Component[] buttons = wordRow.getComponents();
		wordButtons = new JButton[buttons.length];
		for(int i=0;i<buttons.length;i++){
			wordButtons[i]=(JButton) buttons[i];
			wordButtons[i].setEnabled(false);
		}
		keyboard.add(wordRow);
						
		String[] numbers = {"`","1","2","3","4","5","6","7","8","9","0","-","+","Backspace"};
		keyboard.add(rowOfKeys(numbers));
		String[] first = {"Tab","q","w","e","r","t","y","u","i","o","p","[","]","\\"};
		keyboard.add(rowOfKeys(first));
		String[] second = {"Caps Lock","a","s","d","f","g","h","i","j","k","l",";","'","Enter"};
		keyboard.add(rowOfKeys(second));
		String[] third = {"Shift","z","x","c","v","b","n","m",",",".","/","Shift"};
		keyboard.add(rowOfKeys(third));
		String[] fourth = {"Ctrl","Alt","Space","Alt","Ctrl"};
		keyboard.add(rowOfKeys(fourth));
		main.add(keyboard,BorderLayout.CENTER);
		this.getContentPane().add(main);
	}
	/*This function is VERY ugly.  It essentially selects some text and copies 
	 * and pastes it by using KeyPresses.  There's got to be a better way
	 */
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
	private JPanel rowOfKeys(String[] keys) {
		JPanel j = new JPanel(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();

		c.fill = GridBagConstraints.BOTH;
		
		for(int i=0;i<keys.length;i++){
			c.fill = GridBagConstraints.BOTH;
//			c.ipadx = 10;
			c.weighty = 0.5;
			c.weightx = 0.5;
			c.gridwidth = 1;
			c.gridy = 0;
			c.gridx = i;
			button = new JButton(keys[i]);
			button.setFont(button.getFont().deriveFont(20f));
			button.addActionListener(this);
			j.add(button,c);
		}
		return j;
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
		gl.setSize(750, 300);
		gl.setFocusableWindowState(false);

//		try {
//			UIManager.setLookAndFeel(new MotifLookAndFeel());
//		} catch (UnsupportedLookAndFeelException e) {}
		gl.setVisible(true);
	}
	public void actionPerformed(ActionEvent arg0) {
		if(arg0.getSource().getClass().isInstance(button)){
			String key = ((JButton)arg0.getSource()).getText();
			if(key.length()<=0)
				return;
			if(key.charAt(key.length()-1)==' '){
				String cur = text.getText();
				int i = cur.lastIndexOf(' ');
				String temp=key.substring(cur.length()-i-1,key.length()-1);
				text.setText(text.getText()+temp);
				typeString(temp);
				key="Space";
			}
			if(key.equals("Enter")){
				press(KeyEvent.VK_ENTER);
				text.setText("");
				predict();
			}
			else if(key.equals("Space")){
				press(KeyEvent.VK_SPACE);
				text.setText(text.getText()+' ');
				predict();
			}
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
			else if(key.equals("Backspace")){
				String textTyped = text.getText();
				if(textTyped.length()>0)
					text.setText(textTyped.substring(0,textTyped.length()-1));
				press(KeyEvent.VK_BACK_SPACE);
				predict();
			}
			else{
				String temp;
				if(shift)
					temp=""+key.substring(0, 1).toUpperCase()+key.substring(1);
				else
					temp=key;
				text.setText(text.getText()+temp);
				typeString(key);
				predict();
			}
			//myRobot.delay(100);
			//getText();
//			button.setText(""+(char)(button.getText().charAt(0)+1));
		}
	}
	private void predict() {
		String[] results = predictor.getSuggestionsGramBased(predictor.processString(text.getText()), 5);
		for(String r:results) System.out.println(r);
		int i;
		for(i=0;i<results.length;i++){
			wordButtons[i].setText(results[i]+" ");
			wordButtons[i].setEnabled(true);
		}
		for(;i<wordButtons.length;i++){
			wordButtons[i].setText(" ");
			wordButtons[i].setEnabled(false);
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