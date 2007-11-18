package org.ss12.wordprediction.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.ss12.wordprediction.WordLoader;
import org.ss12.wordprediction.WordPredictor;
import org.ss12.wordprediction.model.PredictionModel;

public class GuiLauncher extends JFrame implements ActionListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JButton predictButton;
	JTextField input;
	JList output;
	PredictionModel predictor;
	public GuiLauncher(PredictionModel wp) {
		predictor = wp;
		JPanel main = new JPanel();
		input = new JTextField(20);
		predictButton = new JButton("Predict");
		String[] results = {"cat","cats","cattle","cattles","cattled"};
		output = new JList(results);
		predictButton.addActionListener(this);
		main.add(input);
		main.add(predictButton);
		main.add(output);
		this.getContentPane().add(main);
	}


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		WordLoader wl = new WordLoader(1);
		try {
			wl.loadDictionary(new File("resources/dictionaries/converted/plain.dat"));
			wl.loadFrequenciess(new File("resources/dictionaries/converted/freq.dat"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		PredictionModel wp = new WordPredictor(wl.getWords());
		GuiLauncher gl = new GuiLauncher(wp);
		gl.setDefaultCloseOperation(EXIT_ON_CLOSE);
		gl.setSize(500, 500);
		gl.setVisible(true);
	}


	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(arg0.getSource()==predictButton){
			String beginning = input.getText();
			if(beginning.length()>0){
				String[] results = predictor.getSuggestions(input.getText(), 10);
				output.setListData(results);
			}
			else{
				String[] results = {"Please","Enter","Some","Text","First"};
				output.setListData(results);
			}
		}
	}

}
