package org.ss12.wordprediction.gui.onscreenkeyboard;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Robot;
import java.awt.datatransfer.Clipboard;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

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
	public KeyboardPrototype(Robot robot) {
		myRobot = robot;

		JPanel main = new JPanel();

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				cleanup();
				System.exit(0);
			}
		});
		//main.setLayout(new BoxLayout(main, BoxLayout.PAGE_AXIS));
		text = new JTextField("Hello");
		for(char i='A';i<='Z';i++){
			button = new JButton(""+i);
			button.addActionListener(this);
			main.add(button);
		}
		JButton button2 = new JButton("antidisestablishmentarianism");
		button2.addActionListener(this);
		main.add(button2);
		button2 = new JButton("Enter");
		button2.addActionListener(this);
		main.add(button2);
//		main.add(text);
		this.getContentPane().add(main);
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
		gl.setSize(350, 200);
		gl.setFocusableWindowState(false);

		gl.setVisible(true);
	}
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		if(arg0.getSource().getClass().isInstance(button)){
			if(((JButton)arg0.getSource()).getText().equals("Enter")){
				myRobot.keyPress(KeyEvent.VK_ENTER);
				myRobot.keyRelease(KeyEvent.VK_ENTER);
			}
			else{
				typeString(myRobot, ((JButton)arg0.getSource()).getText());
			}
//			myRobot.keyPress(KeyEvent.VK_A);
//			myRobot.keyRelease(KeyEvent.VK_A);
//			myRobot.keyPress(KeyEvent.VK_SHIFT);
//			myRobot.keyPress(KeyEvent.VK_UP);
//			myRobot.keyRelease(KeyEvent.VK_UP);
//			myRobot.keyRelease(KeyEvent.VK_SHIFT);
//			myRobot.keyPress(KeyEvent.VK_CONTROL);
//			myRobot.keyPress(KeyEvent.VK_C);
//			myRobot.keyRelease(KeyEvent.VK_C);
//			myRobot.keyRelease(KeyEvent.VK_CONTROL);
//			myRobot.keyPress(KeyEvent.VK_DOWN);
//			myRobot.keyRelease(KeyEvent.VK_DOWN);
//			Toolkit tk = Toolkit.getDefaultToolkit();
//			clip = tk.getSystemClipboard();
//			Transferable contents = clip.getContents(null);
//			try {
//				String s = (String)contents.getTransferData(DataFlavor.stringFlavor);
//				text.setText(s);
//			} catch (UnsupportedFlavorException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			//button.setText(""+(char)(button.getText().charAt(0)+1));
		}
	}
	private void typeString(Robot robot, String str) {
		str = str.toLowerCase();
		for(int i=0;i<str.length();i++){
			int key = KeyEvent.VK_A+str.charAt(i)-'a';
			robot.keyPress(key);
			robot.keyRelease(key);
		}
	}

}