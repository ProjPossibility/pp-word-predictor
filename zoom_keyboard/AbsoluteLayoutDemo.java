package absolute;

/*
 * AbsoluteLayoutDemo.java
 * 
 * 
 */

import java.awt.Container;
import java.awt.Insets;
import java.awt.Dimension;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class AbsoluteLayoutDemo {
	Container pane;
	KeyButton[] KeyButtonPositionList = new KeyButton[25];
	KeyboardLayout kl = new KeyboardLayout();
	ArrayList<ArrayList<KeyPosition>> KeyboardLayoutArray = kl.theList; 

	public AbsoluteLayoutDemo(){

		//these five groups of instantiations represent each row of the keyboard
		//each element in the KeyButtonPositionList represents three consecutive keys

		KeyButtonPositionList[0] = new KeyButton(this, new JButton(), new JButton(), new JButton(), "`", "1", "2", KeyboardLayoutArray.get(0));
		KeyButtonPositionList[1] = new KeyButton(this, new JButton(), new JButton(), new JButton(), "3", "4", "5", KeyboardLayoutArray.get(1));
		KeyButtonPositionList[2] = new KeyButton(this, new JButton(), new JButton(), new JButton(), "6", "7", "8", KeyboardLayoutArray.get(2));
		KeyButtonPositionList[3] = new KeyButton(this, new JButton(), new JButton(), new JButton(), "9", "0", "-", KeyboardLayoutArray.get(3));
		KeyButtonPositionList[4] = new KeyButton(this, new JButton(), new JButton(), new JButton(), "=", "Back", "Space", KeyboardLayoutArray.get(4));

		KeyButtonPositionList[5] = new KeyButton(this, new JButton(), new JButton(), new JButton(), "Tab", "Tab", "q", KeyboardLayoutArray.get(5));	
		KeyButtonPositionList[6] = new KeyButton(this, new JButton(), new JButton(), new JButton(), "w", "e", "r", KeyboardLayoutArray.get(6));	
		KeyButtonPositionList[7] = new KeyButton(this, new JButton(), new JButton(), new JButton(), "t", "y", "u", KeyboardLayoutArray.get(7));	
		KeyButtonPositionList[8] = new KeyButton(this, new JButton(), new JButton(), new JButton(), "i", "o", "p", KeyboardLayoutArray.get(8));
		KeyButtonPositionList[9] = new KeyButton(this, new JButton(), new JButton(), new JButton(), "[", "]", "\\", KeyboardLayoutArray.get(9));

		KeyButtonPositionList[10] = new KeyButton(this, new JButton(), new JButton(), new JButton(), "Caps", "Lock", "a", KeyboardLayoutArray.get(10));	
		KeyButtonPositionList[11] = new KeyButton(this, new JButton(), new JButton(), new JButton(), "s", "d", "f", KeyboardLayoutArray.get(11));	
		KeyButtonPositionList[12] = new KeyButton(this, new JButton(), new JButton(), new JButton(), "g", "h", "j", KeyboardLayoutArray.get(12));	
		KeyButtonPositionList[13] = new KeyButton(this, new JButton(), new JButton(), new JButton(), "k", "l", ";", KeyboardLayoutArray.get(13));
		KeyButtonPositionList[14] = new KeyButton(this, new JButton(), new JButton(), new JButton(), "'", "Ent", "er", KeyboardLayoutArray.get(14));

		KeyButtonPositionList[15] = new KeyButton(this, new JButton(), new JButton(), new JButton(), "Shi", "ft", "z", KeyboardLayoutArray.get(15));	
		KeyButtonPositionList[16] = new KeyButton(this, new JButton(), new JButton(), new JButton(), "x", "c", "v", KeyboardLayoutArray.get(16));	
		KeyButtonPositionList[17] = new KeyButton(this, new JButton(), new JButton(), new JButton(), "b", "n", "m", KeyboardLayoutArray.get(17));	
		KeyButtonPositionList[18] = new KeyButton(this, new JButton(), new JButton(), new JButton(), ",", ".", "/", KeyboardLayoutArray.get(18));
		KeyButtonPositionList[19] = new KeyButton(this, new JButton(), new JButton(), new JButton(), "Sh", "i", "ft", KeyboardLayoutArray.get(19));

		KeyButtonPositionList[20] = new KeyButton(this, new JButton(), new JButton(), new JButton(), "Ct", "rl", "Alt", KeyboardLayoutArray.get(20));	
		KeyButtonPositionList[21] = new KeyButton(this, new JButton(), new JButton(), new JButton(), "Sp", "a", "ce", KeyboardLayoutArray.get(21));	
		KeyButtonPositionList[22] = new KeyButton(this, new JButton(), new JButton(), new JButton(), "Sp", "a", "ce", KeyboardLayoutArray.get(22));	
		KeyButtonPositionList[23] = new KeyButton(this, new JButton(), new JButton(), new JButton(), "Sp", "a", "ce", KeyboardLayoutArray.get(23));
		KeyButtonPositionList[24] = new KeyButton(this, new JButton(), new JButton(), new JButton(), "Ct", "rl", "Alt", KeyboardLayoutArray.get(24));

	}	

	//this method is called at the start of the program to create a default layout

	public void KeyboardDraw(){

		for(int i = 0; i < 5; i++){
			for(int j = 0; j < 5; j++){
				KeyPosition kp = new KeyPosition(i*180, j*50, j, i, 50, 180, 1);
				int xCoord = kp.xCoordTopLeft;
				int yCoord = kp.yCoordTopLeft;
				int height = kp.height;
				int width =  kp.width;

				KeyButtonPositionList[i].button1.setBounds(xCoord, yCoord, width/3, height);
				KeyButtonPositionList[i].button2.setBounds(xCoord + width/3, yCoord, width/3, height);
				KeyButtonPositionList[i].button3.setBounds(xCoord + 2*width/3, yCoord, width/3, height);

				pane.add(KeyButtonPositionList[i].button1);
				pane.add(KeyButtonPositionList[i].button2);
				pane.add(KeyButtonPositionList[i].button3);

			}
		}
		//we were having some problems with the layout as it would take the last component
		//added and have it cover the entire window.  adding this panel fixed that.
		pane.add(new JPanel());

	}

	public void KeyboardDraw(ArrayList<KeyPosition> keyList){

		for(int i = 0; i < 25; i++){

			int xCoord = keyList.get(i).getXCoord();
			int yCoord = keyList.get(i).getYCoord();
			int height = keyList.get(i).getHeight();
			int width =  keyList.get(i).getWidth();

			KeyButtonPositionList[i].button1.setBounds(xCoord, yCoord, width/3, height);
			KeyButtonPositionList[i].button2.setBounds(xCoord + width/3, yCoord, width/3, height);
			KeyButtonPositionList[i].button3.setBounds(xCoord + 2*width/3, yCoord, width/3, height);

			pane.add(KeyButtonPositionList[i].button1);
			pane.add(KeyButtonPositionList[i].button2);
			pane.add(KeyButtonPositionList[i].button3);

		}

		//we were having some problems with the layout as it would take the last component
		//added and have it cover the entire window.  adding this panel fixed that.
		//pane.add(new JPanel());
	}
	/**
	 * Create the GUI and show it.  For thread safety,
	 * this method should be invoked from the
	 * event-dispatching thread.
	 */
	private void createAndShowGUI() {
		//Create and set up the window.
		JFrame frame = new JFrame("AbsoluteLayoutDemo");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//Set up the content pane.
		pane = frame.getContentPane();
		KeyboardDraw(KeyboardLayoutArray.get(1));

		//Size and display the window.   
		frame.setSize(1020, 380);
		frame.setVisible(true);
	}

	private void run() {
		//Schedule a job for the event-dispatching thread:
		//creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}

	public static void main(String[] args) throws InterruptedException {
		AbsoluteLayoutDemo ald = new AbsoluteLayoutDemo();
		ald.run();
	}
}
