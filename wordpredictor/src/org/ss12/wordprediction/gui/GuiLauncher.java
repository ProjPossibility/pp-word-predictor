package org.ss12.wordprediction.gui;

import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.TextEvent;
import java.awt.event.TextListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.ss12.wordprediction.WordLoader;
import org.ss12.wordprediction.WordPredictor;
import org.ss12.wordprediction.model.PredictionModel;

public class GuiLauncher extends JFrame implements ActionListener,TextListener, ListSelectionListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JButton predictButton;
	TextField input;
	JList output;
	PredictionModel predictor;
	public GuiLauncher(PredictionModel wp) {
		predictor = wp;
		JPanel main = new JPanel();
		input = new TextField(20);
		predictButton = new JButton("Predict");
		String[] results = {"cat","cats","cattle","cattles","cattled"};
		output = new JList(results);
		predictButton.addActionListener(this);
		input.addTextListener(this);
		output.addListSelectionListener(this);
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


	@Override
	public void textValueChanged(TextEvent arg0) {
		if(arg0.getSource()==input){
			String beginning = input.getText();
			if(beginning.length()>0){
				String[] text = input.getText().split(" ");
				String[] results = predictor.getSuggestions(text[text.length-1], 10);
				output.setListData(results);
			}
			else{
				String[] results = {"Please","Enter","Some","Text","First"};
				output.setListData(results);
			}
		}
	}


	@Override
	public void valueChanged(ListSelectionEvent arg0) {
		if(arg0.getSource()==output){
			String t = (String)output.getSelectedValue();
			if(t!=null){
				int j = input.getText().lastIndexOf(" ");
				if(j==-1){
					input.setText(t+" ");
				}
				else{
					String beginning = input.getText().substring(0,j);
					input.setText(beginning+" "+t+" ");
				}
				input.requestFocus();
				input.setCaretPosition(input.getText().length());
			}
		}
	}

}
