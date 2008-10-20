/*
 *  Sets the text on the keys.
 *  On mouseEntered event for every key, calls the KeyboardDraw function to re-draw the keyboard layout
 */

package absolute;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JButton;

public class KeyButton implements MouseListener{

	JButton button1;
	JButton button2;
	JButton button3;
	ArrayList<KeyPosition> keyList;
	
	AbsoluteLayoutDemo ald;

	public KeyButton(AbsoluteLayoutDemo a, JButton b1, JButton b2, JButton b3, String s1, String s2, String s3, ArrayList<KeyPosition> positionList){
	
		ald = a;
		
		button1 = b1;
		button2 = b2;
		button3 = b3;
		button1.setText(s1);
		button2.setText(s2);
		button3.setText(s3);
		keyList = positionList;

		button1.addMouseListener(this);
		button2.addMouseListener(this);
		button3.addMouseListener(this);
		
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		// need to set text in text field
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		
		ald.KeyboardDraw(keyList);
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}
}


