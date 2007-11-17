package org.ss12.wordprediction.gui;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class GuiLauncher extends JFrame{
	
	public GuiLauncher() {
		// TODO Auto-generated constructor stub
		JPanel main = new JPanel();
		JTextField input = new JTextField(20);
		JButton predict = new JButton("Predict");
		JTextArea text = new JTextArea(5,40);
		main.add(input);
		main.add(predict);
		main.add(text);
		this.getContentPane().add(main);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		GuiLauncher gl = new GuiLauncher();
		gl.setSize(500, 500);
		gl.setVisible(true);
	}

}
