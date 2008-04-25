package org.ss12.wordprediction.gui.onscreenkeyboard;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Robot;
import java.awt.datatransfer.Clipboard;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.AbstractButton;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;

import org.ss12.wordprediction.TreeMapWordPredictor;
import org.ss12.wordprediction.gui.onscreenkeyboard.components.KeyButton;
import org.ss12.wordprediction.model.WordPredictor;

public class KeyboardPrototype extends JFrame implements ActionListener, MouseListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int NUM_OF_WORDS = 5;
	Clipboard clip;
	JTextField text;
	Robot virtualKeyboard;
	JButton[] wordButtons;
	WordPredictor predictor;
	JPanel body;
	private boolean shift;
	private boolean capslock;
	private boolean alt;
	private boolean ctrl;
	Thread loading;
	JPanel predictionRow;
	JToggleButton leftShiftButton,rightShiftButton,leftCtrlButton,rightCtrlButton,leftAltButton,rightAltButton,capslockButton;
	Font selectedFont,regularFont;
	Color buttonColor = new javax.swing.plaf.ColorUIResource(0);

	public KeyboardPrototype(Robot robot) {
		super("Word Prediction On Screen Keyboard");
		// Set Mac OS X to use the standard look and feel of Java and not the native Aqua user interface
//		try {
//			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
//		} catch (ClassNotFoundException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		} catch (InstantiationException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		} catch (IllegalAccessException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		} catch (UnsupportedLookAndFeelException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
		loading = new Thread(){
			public void run(){
				predictor = new TreeMapWordPredictor();
				predict();
				learn();
			}
		};
		loading.start();
		virtualKeyboard = robot;
		shift=false;
		JPanel main = new JPanel();
		JMenuBar menu = new JMenuBar();
		JMenu view = new JMenu("View");
		JCheckBoxMenuItem viewPredictions = new JCheckBoxMenuItem("Predictions",true);
		JCheckBoxMenuItem viewKeyboard = new JCheckBoxMenuItem("Keyboard",true);
		viewPredictions.addActionListener(this);
		viewKeyboard.addActionListener(this);
		view.add(viewPredictions);
		view.add(viewKeyboard);
		menu.add(view);
		
		this.setJMenuBar(menu);
		
		regularFont = new Font("Times New Roman",Font.PLAIN,20);
		selectedFont = new Font("Times New Roman",Font.BOLD,20);

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

		body = new JPanel();

		body.setLayout(new BoxLayout(body, BoxLayout.PAGE_AXIS));

		String[] words = new String[NUM_OF_WORDS];
		int[] wordsKeycodes = new int[NUM_OF_WORDS];
		for(int i=0;i<NUM_OF_WORDS;i++){
			words[i]=" ";
			wordsKeycodes[i] = 0;
		}
		predictionRow = rowOfKeys(words,words,wordsKeycodes);
		Component[] buttons = predictionRow.getComponents();
		wordButtons = new JButton[buttons.length];
		for(int i=0;i<buttons.length;i++){
			wordButtons[i]=(JButton) buttons[i];
			wordButtons[i].setEnabled(false);
		}
		body.add(predictionRow);
		
		String[] numbers = {"`","1","2","3","4","5","6","7","8","9","0","-","=","Backspace"};
		String[] upperNumbers = {"~","!","@","#","$","%","^","&","*","(",")","_","+","Backspace"};

		int[] numberKeycodes = {KeyEvent.VK_BACK_QUOTE,KeyEvent.VK_1,KeyEvent.VK_2,KeyEvent.VK_3,KeyEvent.VK_4,KeyEvent.VK_5,KeyEvent.VK_6,KeyEvent.VK_7,KeyEvent.VK_8,KeyEvent.VK_9,KeyEvent.VK_0,KeyEvent.VK_MINUS,KeyEvent.VK_EQUALS,KeyEvent.VK_BACK_SPACE};
		body.add(rowOfKeys(numbers,upperNumbers,numberKeycodes));
		String[] first = {"Tab","q","w","e","r","t","y","u","i","o","p","[","]","\\"};
		String[] upperFirst = {"Tab","Q","W","E","R","T","Y","U","I","O","P","{","}","|"};
		int[] firstKeycodes = {KeyEvent.VK_TAB,KeyEvent.VK_Q,KeyEvent.VK_W,KeyEvent.VK_E,KeyEvent.VK_R,KeyEvent.VK_T,KeyEvent.VK_Y,KeyEvent.VK_U,KeyEvent.VK_I,KeyEvent.VK_O,KeyEvent.VK_P,KeyEvent.VK_OPEN_BRACKET,KeyEvent.VK_CLOSE_BRACKET,KeyEvent.VK_BACK_SLASH};
		body.add(rowOfKeys(first,upperFirst,firstKeycodes));
		String[] second = {"Caps Lock","a","s","d","f","g","h","j","k","l",";","'","Enter"};
		String[] upperSecond = {"Caps Lock","A","S","D","F","G","H","J","K","L",":",""+'"',"Enter"};
		int[] secondKeycodes = {KeyEvent.VK_CAPS_LOCK,KeyEvent.VK_A,KeyEvent.VK_S,KeyEvent.VK_D,KeyEvent.VK_F,KeyEvent.VK_G,KeyEvent.VK_H,KeyEvent.VK_J,KeyEvent.VK_K,KeyEvent.VK_L,KeyEvent.VK_SEMICOLON,KeyEvent.VK_QUOTE,KeyEvent.VK_ENTER};
		body.add(rowOfKeys(second,upperSecond,secondKeycodes));
		String[] third = {"Shift","z","x","c","v","b","n","m",",",".","/","Shift"};
		String[] upperThird = {"Shift","Z","X","C","V","B","N","M","<",">","?","Shift"};
		int[] thirdKeycodes = {KeyEvent.VK_SHIFT,KeyEvent.VK_Z,KeyEvent.VK_X,KeyEvent.VK_C,KeyEvent.VK_V,KeyEvent.VK_B,KeyEvent.VK_N,KeyEvent.VK_M,KeyEvent.VK_COMMA,KeyEvent.VK_PERIOD,KeyEvent.VK_SLASH,KeyEvent.VK_SHIFT};
		body.add(rowOfKeys(third,upperThird,thirdKeycodes));
		String[] fourth = {"Ctrl","Alt","Space","Alt","Ctrl"};
		int[] fourthKeycodes = {KeyEvent.VK_CONTROL,KeyEvent.VK_ALT,KeyEvent.VK_SPACE,KeyEvent.VK_ALT,KeyEvent.VK_CONTROL};
		body.add(rowOfKeys(fourth,fourth,fourthKeycodes));
		main.add(body,BorderLayout.CENTER);
		this.getContentPane().add(main);
	}
	private JPanel rowOfKeys(String[] keys, String[] upperKeys, int[] keycode) {
		JPanel j = new JPanel(new GridBagLayout());
		AbstractButton button;
		GridBagConstraints c = new GridBagConstraints();

		c.fill = GridBagConstraints.BOTH;

		for(int i=0;i<keys.length;i++){
			c.fill = GridBagConstraints.BOTH;
			c.ipadx = 10;
			c.weighty = 0.5;
			c.weightx = 0.5;
			c.gridwidth = 1;
			c.gridy = 0;
			c.gridx = i;
			if(keycode[i]==KeyEvent.VK_CAPS_LOCK){
				button = capslockButton = new JToggleButton(keys[i]);
			}
			else if(keycode[i]==KeyEvent.VK_SHIFT){
				if(leftShiftButton==null)
					button = leftShiftButton = new JToggleButton(keys[i]);
				else
					button = rightShiftButton = new JToggleButton(keys[i]);
			}
			else if(keycode[i]==KeyEvent.VK_ALT){
				if(leftAltButton==null)
					button = leftAltButton = new JToggleButton(keys[i]);
				else
					button = rightAltButton = new JToggleButton(keys[i]);
			}
			else if(keycode[i]==KeyEvent.VK_CONTROL){
				if(leftCtrlButton==null)
					button = leftCtrlButton = new JToggleButton(keys[i]);
				else
					button = rightCtrlButton = new JToggleButton(keys[i]);
			}
			else if(keycode[i]==0){
				button = new JButton(keys[i]);
			}
			else{
				button = new KeyButton(keys[i],upperKeys[i],keycode[i]);
			}
			button.setFont(regularFont);
			button.addActionListener(this);
			button.addMouseListener(this);
			j.add(button,c);
		}
		return j;
	}
	public void cleanup(){
		this.setVisible(false);
		loading=null;
		if(predictor==null)
			return;
		predictor.cleanup();
	}


	/**
	 * @param args
	 */
	public static void main(String[] args) throws AWTException{
		KeyboardPrototype gl = new KeyboardPrototype(new Robot());
		gl.setDefaultCloseOperation(EXIT_ON_CLOSE);
		gl.setAlwaysOnTop(true);
		gl.setSize(800, 300);
		gl.setFocusableWindowState(false);
//		gl.getRootPane().setFocusable(false);
	
		
//		try {
//		UIManager.setLookAndFeel(new MotifLookAndFeel());
//		} catch (UnsupportedLookAndFeelException e) {}
		gl.setVisible(true);
	}
	public void actionPerformed(ActionEvent arg0) {
		if(arg0.getSource() instanceof KeyButton){
			KeyButton key = ((KeyButton)arg0.getSource());
			int keycode = key.getKeyCode();
			if(keycode==KeyEvent.VK_BACK_SPACE){
				String textTyped = text.getText();
				if(textTyped.length()>0)
					text.setText(textTyped.substring(0,textTyped.length()-1));
			}
			else if(keycode==KeyEvent.VK_TAB || keycode==KeyEvent.VK_ENTER){
				text.setText("");
			}
			else if(keycode==KeyEvent.VK_SPACE){
				text.setText(text.getText()+' ');
			}
			else{
				text.setText(text.getText()+key.getText());
			}
			press(keycode);
			predict();
			learn();
		}
		else if(arg0.getSource() instanceof JToggleButton){
			JToggleButton key = ((JToggleButton)arg0.getSource());
			String text = key.getText();
			if(text.equals("Caps Lock")){
				capslock = key.isSelected();
				shift(shift);
				press(KeyEvent.VK_CAPS_LOCK);
			}
			else if(text.equals("Shift")){
				shift(key.isSelected());
				if(shift)
					virtualKeyboard.keyPress(KeyEvent.VK_SHIFT);
				else
					virtualKeyboard.keyRelease(KeyEvent.VK_SHIFT);
			}
			else if(text.equals("Alt")){
				alt(key.isSelected());
				if(alt)
					virtualKeyboard.keyPress(KeyEvent.VK_ALT);
				else
					virtualKeyboard.keyRelease(KeyEvent.VK_ALT);
			}
			else if(text.equals("Ctrl")){
				ctrl(key.isSelected());
				if(ctrl)
					virtualKeyboard.keyPress(KeyEvent.VK_CONTROL);
				else
					virtualKeyboard.keyRelease(KeyEvent.VK_CONTROL);
			}
		}
		else if(arg0.getSource() instanceof JButton){
			String cur = text.getText();
			int i = cur.lastIndexOf(' ');
			String key = ((JButton)arg0.getSource()).getText();
			String temp=key.substring(cur.length()-i-1,key.length());
			text.setText(text.getText()+temp+' ');
			typeString(temp);
			press(KeyEvent.VK_SPACE);
			predict();
			learn();
		}
		else if(arg0.getSource() instanceof JCheckBoxMenuItem){
			JCheckBoxMenuItem cur = (JCheckBoxMenuItem)arg0.getSource();
			String text = cur.getText();
			if(text.equals("Predictions")){
				predictionRow.setVisible(cur.getState());
			}
			else if(text.equals("Keyboard")){
				Component[] c = body.getComponents();
				for(int i=1;i<c.length;i++)
					c[i].setVisible(cur.getState());
			}
			else if(text.equals("Recent Text")){
				this.text.setVisible(cur.getState());
			}
		}
	}
	private void shift(boolean selected) {
		shift=selected;
		rightShiftButton.setSelected(selected);
		leftShiftButton.setSelected(selected);
		Component[] panels = body.getComponents();
		for(Component p:panels){
			if(p instanceof JPanel){
				Component[] buttons = ((JPanel)p).getComponents();
				for(Component b:buttons){
					if(b instanceof KeyButton){
						KeyButton kb = (KeyButton)b;
						if(kb.getKeyCode()>=KeyEvent.VK_A && kb.getKeyCode()<=KeyEvent.VK_Z){
							kb.setShift(selected || capslock);
						}
						else
							kb.setShift(selected);
					}
				}
			}
		}
		for(JButton b:wordButtons){
			String text = b.getText();
			if(capslock)
				b.setText(text.toUpperCase());
			else if(selected && b.getText().length()>0)
				b.setText(text.substring(0,1).toUpperCase()+text.substring(1));
			else
				b.setText(text.toLowerCase());
		}
	}
	private void learn() {
		if(predictor==null)
			return;
		String[] buffer = predictor.processString(text.getText());
		if(buffer[buffer.length-1].equals("")){
			predictor.learn(text.getText());
		}
	}
	private void predict() {
		if(predictor==null)
			return;
		loading = new Thread(){
			public void run(){
				String[] results = predictor.getSuggestionsGramBased(predictor.processString(text.getText()), NUM_OF_WORDS);
				int i;
				for(i=0;i<results.length;i++){
					if(capslock)
						results[i] = results[i].toUpperCase();
					wordButtons[i].setText(results[i]);
					wordButtons[i].setEnabled(true);
					wordButtons[i].setBackground(buttonColor);
				}
				for(;i<wordButtons.length;i++){
					wordButtons[i].setText(" ");
					wordButtons[i].setEnabled(false);
					wordButtons[i].setBackground(Color.white);					
				}			
			}
		};
		loading.start();
	}
	private void press(int key) {
		if(shift)
			virtualKeyboard.keyPress(KeyEvent.VK_SHIFT);
		if(alt)
			virtualKeyboard.keyPress(KeyEvent.VK_ALT);
		if(ctrl)
			virtualKeyboard.keyPress(KeyEvent.VK_CONTROL);
		virtualKeyboard.keyPress(key);
		//might need to add a delay here?  Works on Ubuntu/XP
		virtualKeyboard.keyRelease(key);
		if(shift){
			virtualKeyboard.keyRelease(KeyEvent.VK_SHIFT);
			shift(false);
		}
		if(alt){
			virtualKeyboard.keyRelease(KeyEvent.VK_ALT);
			alt(false);
		}
		if(ctrl){
			virtualKeyboard.keyRelease(KeyEvent.VK_CONTROL);
			ctrl(false);
		}
	}
	private void alt(boolean selected) {
		alt=selected;
		rightAltButton.setSelected(selected);
		leftAltButton.setSelected(selected);
	}
	private void ctrl(boolean selected) {
		ctrl=selected;
		rightCtrlButton.setSelected(selected);
		leftCtrlButton.setSelected(selected);
	}
	private void typeString(String str) {
		str = str.toLowerCase();
		for(int i=0;i<str.length();i++){
			int key = str.toUpperCase().charAt(i);
			if(key>='A' && key<='Z')
				press(key);
			else if(key=='\'')
				press(KeyEvent.VK_QUOTE);
			else if(key=='-')
				press(KeyEvent.VK_MINUS);
		}
	}
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	public void mouseEntered(MouseEvent e) {
		if(e.getSource() instanceof JComponent){
			JComponent current = (JComponent)e.getSource();
			if(current.isEnabled()){
				current.setFont(selectedFont);
				current.setBackground(Color.white);
			}
//			current.setBackground(new javax.swing.plaf.ColorUIResource(Color.white));
		}
	}
	public void mouseExited(MouseEvent e) {
		if(e.getSource() instanceof JComponent){
			JComponent current = (JComponent)e.getSource();
			if(current.isEnabled()){
				current.setFont(regularFont);
				current.setBackground(buttonColor);
			}
		}
	}  
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}