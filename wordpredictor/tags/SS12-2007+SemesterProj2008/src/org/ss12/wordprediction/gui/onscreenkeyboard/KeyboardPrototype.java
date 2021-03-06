package org.ss12.wordprediction.gui.onscreenkeyboard;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Robot;
import java.awt.datatransfer.Clipboard;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.AbstractButton;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;

import org.ss12.wordprediction.TreeMapWordPredictor;
import org.ss12.wordprediction.gui.onscreenkeyboard.components.KeyButton;
import org.ss12.wordprediction.model.WordPredictor;
import org.ss12.wordprediction.servlet.JsonWordPredictor;
import org.ss12.wordprediction.servlet.NewWordPredictorAdapter;

public class KeyboardPrototype extends JFrame implements ActionListener, MouseListener, ComponentListener{
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
	private boolean isLearning;
	JPanel predictionRow;
	JToggleButton leftShiftButton,rightShiftButton,leftCtrlButton,rightCtrlButton,leftAltButton,rightAltButton,capslockButton;
	Font regularFont;
	Color buttonColor = new javax.swing.plaf.ColorUIResource(0);

	public KeyboardPrototype(Robot robot, final boolean useWeb) {
		super("Word Prediction On Screen Keyboard");
		// Set Mac OS X to use the standard look and feel of Java and not the native Aqua user interface
//		try {
//		UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
//		} catch (ClassNotFoundException e1) {
//		// TODO Auto-generated catch block
//		e1.printStackTrace();
//		} catch (InstantiationException e1) {
//		// TODO Auto-generated catch block
//		e1.printStackTrace();
//		} catch (IllegalAccessException e1) {
//		// TODO Auto-generated catch block
//		e1.printStackTrace();
//		} catch (UnsupportedLookAndFeelException e1) {
//		// TODO Auto-generated catch block
//		e1.printStackTrace();
//		}
		loading = new Thread(){
			public void run(){
				if (useWeb) {
					try {
						predictor = new NewWordPredictorAdapter(new JsonWordPredictor(
								new URI("http://scecwork.dyndns.org:8080/predictservice")));
						// new URI("http://localhost:8080/predictservice")));
					} catch (URISyntaxException e) {
						// Should never happen.
						System.err.println("Invalid URI: " + e);
						System.exit(-1);
					}
				} else {
					predictor = new TreeMapWordPredictor();
				}
				predict();
				learn();
			}
		};
		isLearning=true;
		this.addComponentListener(this);
		virtualKeyboard = robot;
		shift=false;
		JPanel main = new JPanel();
		JMenuBar menu = new JMenuBar();
		JMenu view = new JMenu("View");
		JMenu settings = new JMenu("Settings");
		JCheckBoxMenuItem viewPredictions = new JCheckBoxMenuItem("Predictions",true);
		JCheckBoxMenuItem viewKeyboard = new JCheckBoxMenuItem("Keyboard",true);
		JCheckBoxMenuItem viewRecentText = new JCheckBoxMenuItem("Recent Text",true);
		JMenuItem setFont = new JMenuItem("Font");
		JCheckBoxMenuItem setLearn = new JCheckBoxMenuItem("Learn",isLearning);
		JCheckBoxMenuItem setIntelligent = new JCheckBoxMenuItem("Intellitext",true);
		viewPredictions.addActionListener(this);
		viewKeyboard.addActionListener(this);
		viewRecentText.addActionListener(this);
		setFont.addActionListener(this);
		setLearn.addActionListener(this);
		setIntelligent.addActionListener(this);
		view.add(viewPredictions);
		view.add(viewKeyboard);
		view.add(viewRecentText);
		//settings.add(setFont);
		settings.add(setLearn);
		settings.add(setIntelligent);
		menu.add(view);
		menu.add(settings);

		this.setJMenuBar(menu);

		regularFont = new Font("Arial",Font.BOLD,20);
		//selectedFont = new Font("Arial",Font.BOLD,20);

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				cleanup();
				System.exit(0);
			}
		});
		main.setLayout(new BorderLayout());
		text = new JTextField("");
		text.setFont(regularFont);
		text.setMinimumSize(new Dimension(5,5));
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
			wordButtons[i].setText("Loading...");
		}
		body.add(predictionRow);
		loading.start();

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
		String[] third = {"Shift","z","x","c","v","b","n","m",",",".","<body>/</body>","Shift"};
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
			c.weightx = 1.0;
			c.gridwidth = 1;
			c.gridy = 0;
			c.gridx = i;
			if(keycode[i]==KeyEvent.VK_CAPS_LOCK){
				button = capslockButton = new JToggleButton(keys[i]);
				c.weightx = 2.0;
			}
			else if(keycode[i]==KeyEvent.VK_SHIFT){
				if(leftShiftButton==null)
					button = leftShiftButton = new JToggleButton(keys[i]);
				else
					button = rightShiftButton = new JToggleButton(keys[i]);
				c.weightx=3.5;
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
				button = new JButton("<html>"+keys[i]+"</html>");
			}
			else{
				button = new KeyButton("<html>"+keys[i]+"</html>","<html>"+upperKeys[i]+"</html>",keycode[i]);
				if(keycode[i]==KeyEvent.VK_TAB)
					c.weightx=1.5;
				else if(keycode[i]==KeyEvent.VK_BACK_SPACE)
					c.weightx=2.5;
				else if(keycode[i]==KeyEvent.VK_ENTER)
					c.weightx=3.0;
				else if(keycode[i]==KeyEvent.VK_SPACE)
					c.weightx=2.5;
			}
			button.setMinimumSize(new Dimension(5,5));
			button.setFont(regularFont);
			button.addActionListener(this);
			button.addMouseListener(this);
			button.setMargin(new Insets(0,0,0,0));
			j.add(button,c);
		}
		return j;
	}
	public void cleanup(){
		this.setVisible(false);
		loading=null;
		newest=null;
		if(predictor!=null)
			predictor.cleanup();
		System.exit(0);
	}


	/**
	 * @param args
	 */
	public static void main(String[] args) throws AWTException{
		boolean useWeb = false;
		for (String arg : args) {
			if (arg.equals("web")) {
				useWeb = true;
			}
		}

		KeyboardPrototype gl = new KeyboardPrototype(new Robot(), useWeb);
		gl.setDefaultCloseOperation(EXIT_ON_CLOSE);
		gl.setAlwaysOnTop(true);
		gl.setSize(800, 300);
//		gl.getRootPane().setFocusable(false);


//		try {
//		UIManager.setLookAndFeel(new MotifLookAndFeel());
//		} catch (UnsupportedLookAndFeelException e) {}
		gl.setVisible(true);
		gl.setFocusableWindowState(false);
	}
	public void actionPerformed(ActionEvent arg0) {
		if(arg0.getSource() instanceof KeyButton){
			KeyButton key = ((KeyButton)arg0.getSource());
			int keycode = key.getKeyCode();
			String keyText = key.getText().replace("<html>", "").replace("</html>", "").replace("<body>","").replace("</body>", "");
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
			//if a character the denotes the end of a sentence
			else if(keycode==KeyEvent.VK_PERIOD || keycode==KeyEvent.VK_COMMA || 
					(shift && (keycode==KeyEvent.VK_SLASH || 
							keycode==KeyEvent.VK_1))){
				String textTyped = text.getText();
				int length = textTyped.length();
				if(length>0 && textTyped.charAt(length-1)==' '){
					boolean storeShift = shift;
					press(KeyEvent.VK_BACK_SPACE);
					shift = storeShift;
					press(keycode);
					press(KeyEvent.VK_SPACE);
					text.setText(textTyped.substring(0, length-1)+keyText+' ');
					predict();
					learn();
					return;
				}
				else{
					text.setText(text.getText()+keyText);
				}
			}
			else{
				text.setText(text.getText()+keyText);
			}
			press(keycode);
			predict();
			learn();
		}
		else if(arg0.getSource() instanceof JToggleButton){
			JToggleButton key = ((JToggleButton)arg0.getSource());
			String text = key.getText().replace("<html>", "").replace("</html>", "");
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
			String temp=key.substring(cur.length()-i-1,key.length()).replace("<html>", "").replace("</html>", "").replace("<body>","").replace("</body>","");
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
			else if(text.equals("Font")){
//				regularFont = NwFontChooserS.showDialog(this, null, regularFont);
//				componentResized(null);
			}
			else if(text.equals("Learn")){
				isLearning=cur.getState();
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
		if(predictor==null || !isLearning)
			return;
		String[] buffer = predictor.processString(text.getText());
		if(buffer[buffer.length-1].equals("")){
			predictor.learn(text.getText());
		}
	}
	Thread newest;
	private void predict() {
		if(predictor==null)
			return;

		loading = new Thread(){
			public void run(){
				try{
					String[] results = predictor.getSuggestionsGramBased(predictor.processString(text.getText()), NUM_OF_WORDS);
					if(Thread.currentThread()==newest){
						newest=null;
						int i=0;
						for(;i<results.length;i++){
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
				}catch(ThreadDeath td){
					System.out.println("Caught ThreadDeath");
				}
			}
		};
		newest=loading;
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
//				current.setFont(selectedFont);
				current.setBackground(Color.white);
			}
//			current.setBackground(new javax.swing.plaf.ColorUIResource(Color.white));
		}
	}
	public void mouseExited(MouseEvent e) {
		if(e.getSource() instanceof JComponent){
			JComponent current = (JComponent)e.getSource();
			if(current.isEnabled()){
				//current.setFont(regularFont);
				current.setBackground(buttonColor);
			}
		}
	}
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}
	public void componentHidden(ComponentEvent e) {
		// TODO Auto-generated method stub

	}
	public void componentMoved(ComponentEvent e) {
		// TODO Auto-generated method stub

	}
	public void componentResized(ComponentEvent e) {
		float font = ((this.getWidth()*2+this.getHeight())/100.0f)*1.25f;
		regularFont = regularFont.deriveFont(font);
//		selectedFont = selectedFont.deriveFont(font);
		Component[] panels = body.getComponents();
		for(Component p:panels){
			if(p instanceof JPanel){
				Component[] buttons = ((JPanel)p).getComponents();
				for(Component b:buttons){
					b.setFont(regularFont);
				}
			}
		}
		for(JButton b:wordButtons){
			b.setFont(regularFont);
		}
		text.setFont(regularFont);
	}
	public void componentShown(ComponentEvent e) {
		// TODO Auto-generated method stub

	}  
}