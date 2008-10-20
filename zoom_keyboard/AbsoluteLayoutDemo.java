package absolute;

/*
 * AbsoluteLayoutDemo.java requires no other files.
 */

import java.awt.Container;
import java.awt.Insets;
import java.awt.Dimension;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class AbsoluteLayoutDemo {
	Container pane;
	KeyButton[] KeyButtonPositionList = new KeyButton[25];
	KeyboardLayout kl = new KeyboardLayout();
	KeyPosition[] KeyboardLayoutArray = kl.getLayoutList(); 
		
	public AbsoluteLayoutDemo(){
		
		//these five groups of instantiations represent each row of the keyboard
		//each element in the KeyButtonPositionList represents three consecutive keys
		
		KeyButtonPositionList[0] = new KeyButton(new JButton(), new JButton(), new JButton(), "`", "1", "2", KeyboardLayoutArray[0]);
		KeyButtonPositionList[1] = new KeyButton(new JButton(), new JButton(), new JButton(), "3", "4", "5", KeyboardLayoutArray[1]);
		KeyButtonPositionList[2] = new KeyButton(new JButton(), new JButton(), new JButton(), "6", "7", "8", KeyboardLayoutArray[2]);
		KeyButtonPositionList[3] = new KeyButton(new JButton(), new JButton(), new JButton(), "9", "0", "-", KeyboardLayoutArray[3]);
		KeyButtonPositionList[4] = new KeyButton(new JButton(), new JButton(), new JButton(), "=", "Back", "Space", KeyboardLayoutArray[4]);
	
		KeyButtonPositionList[5] = new KeyButton(new JButton(), new JButton(), new JButton(), "Tab", "Tab", "q", KeyboardLayoutArray[5]);	
		KeyButtonPositionList[6] = new KeyButton(new JButton(), new JButton(), new JButton(), "w", "e", "r", KeyboardLayoutArray[6]);	
		KeyButtonPositionList[7] = new KeyButton(new JButton(), new JButton(), new JButton(), "t", "y", "u", KeyboardLayoutArray[7]);	
		KeyButtonPositionList[8] = new KeyButton(new JButton(), new JButton(), new JButton(), "i", "o", "p", KeyboardLayoutArray[8]);
		KeyButtonPositionList[9] = new KeyButton(new JButton(), new JButton(), new JButton(), "[", "]", "\\", KeyboardLayoutArray[9]);
		
		KeyButtonPositionList[10] = new KeyButton(new JButton(), new JButton(), new JButton(), "Caps", "Lock", "a", KeyboardLayoutArray[10]);	
		KeyButtonPositionList[11] = new KeyButton(new JButton(), new JButton(), new JButton(), "s", "d", "f", KeyboardLayoutArray[11]);	
		KeyButtonPositionList[12] = new KeyButton(new JButton(), new JButton(), new JButton(), "g", "h", "j", KeyboardLayoutArray[12]);	
		KeyButtonPositionList[13] = new KeyButton(new JButton(), new JButton(), new JButton(), "k", "l", ";", KeyboardLayoutArray[13]);
		KeyButtonPositionList[14] = new KeyButton(new JButton(), new JButton(), new JButton(), "'", "Ent", "er", KeyboardLayoutArray[14]);
		
		KeyButtonPositionList[15] = new KeyButton(new JButton(), new JButton(), new JButton(), "Shi", "ft", "z", KeyboardLayoutArray[15]);	
		KeyButtonPositionList[16] = new KeyButton(new JButton(), new JButton(), new JButton(), "x", "c", "v", KeyboardLayoutArray[16]);	
		KeyButtonPositionList[17] = new KeyButton(new JButton(), new JButton(), new JButton(), "b", "n", "m", KeyboardLayoutArray[17]);	
		KeyButtonPositionList[18] = new KeyButton(new JButton(), new JButton(), new JButton(), ",", ".", "/", KeyboardLayoutArray[18]);
		KeyButtonPositionList[19] = new KeyButton(new JButton(), new JButton(), new JButton(), "Sh", "i", "ft", KeyboardLayoutArray[19]);
		
		KeyButtonPositionList[20] = new KeyButton(new JButton(), new JButton(), new JButton(), "Ct", "rl", "Alt", KeyboardLayoutArray[20]);	
		KeyButtonPositionList[21] = new KeyButton(new JButton(), new JButton(), new JButton(), "Sp", "a", "ce", KeyboardLayoutArray[21]);	
		KeyButtonPositionList[22] = new KeyButton(new JButton(), new JButton(), new JButton(), "Sp", "a", "ce", KeyboardLayoutArray[22]);	
		KeyButtonPositionList[23] = new KeyButton(new JButton(), new JButton(), new JButton(), "Sp", "a", "ce", KeyboardLayoutArray[23]);
		KeyButtonPositionList[24] = new KeyButton(new JButton(), new JButton(), new JButton(), "Ct", "rl", "Alt", KeyboardLayoutArray[24]);
	
	}	
	
	public void KeyboardDraw(){
		
		for(int i = 0; i < 25; i++){
			
			int xCoord = KeyButtonPositionList[i].kp.xCoordTopLeft;
			int yCoord = KeyButtonPositionList[i].kp.yCoordTopLeft;
			int height = KeyButtonPositionList[i].kp.height;
			int width = KeyButtonPositionList[i].kp.width;
			
			KeyButtonPositionList[i].button1.setBounds(xCoord, yCoord, height, width/3);
			KeyButtonPositionList[i].button2.setBounds(xCoord + width/3, yCoord, height, width/3);
			KeyButtonPositionList[i].button3.setBounds(xCoord + 2*width/3, yCoord, height, width/3);
			
			pane.add(KeyButtonPositionList[i].button1);
			pane.add(KeyButtonPositionList[i].button2);
			pane.add(KeyButtonPositionList[i].button3);
		
		}
		
		//we were having some problems with the layout as it would take the last component
		//added and have it cover the entire window.  adding this panel fixed that.
		pane.add(new JPanel());
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
        KeyboardDraw();

        //Size and display the window.   
        frame.setSize(900, 300);
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
