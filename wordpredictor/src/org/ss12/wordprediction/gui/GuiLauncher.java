/*
    This file is part of Word Predictor.

    Word Predictor is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Word Predictor is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Word Predictor.  If not, see <http://www.gnu.org/licenses/>. 

    This software was developed by members of Project:Possibility, a software 
    collaboration for the disabled.

    For more information, visit http://projectpossibility.org
 */

package org.ss12.wordprediction.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.ss12.wordprediction.WordPredictor;
import org.ss12.wordprediction.model.PredictionModel;

public class GuiLauncher extends JFrame implements ActionListener, ListSelectionListener, KeyListener, DocumentListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JButton predictButton;
	JTextField input;
	JList output;
	JList outputBi;
	JList outputTri;
	JPopupMenu menuOutput;
	JMenuItem[] items;
	PredictionModel predictor;
	JScrollPane jScrollPane1;
	JScrollPane jScrollPane2;
	JScrollPane jScrollPane3;
	public GuiLauncher(PredictionModel wp) {
		JPanel main = new JPanel();

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				cleanup();
				System.exit(0);
			}
		});
		input = new JTextField();
		input.addKeyListener(this);
		jScrollPane1 = new javax.swing.JScrollPane();
		output = new javax.swing.JList();
		output.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jScrollPane2 = new javax.swing.JScrollPane();
		outputBi = new javax.swing.JList();    
		outputBi.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jScrollPane3 = new javax.swing.JScrollPane();
		outputTri = new javax.swing.JList();
		outputTri.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		output.setModel(new javax.swing.AbstractListModel() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 6218654733508813417L;
			String[] strings = {};
			public int getSize() { return strings.length; }
			public Object getElementAt(int i) { return strings[i]; }
		});
		jScrollPane1.setViewportView(output);

		outputBi.setModel(new javax.swing.AbstractListModel() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 5152614925643522305L;
			String[] strings = {};
			public int getSize() { return strings.length; }
			public Object getElementAt(int i) { return strings[i]; }
		});
		jScrollPane2.setViewportView(outputBi);

		outputTri.setModel(new javax.swing.AbstractListModel() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 907980171513869749L;
			String[] strings = {};
			public int getSize() { return strings.length; }
			public Object getElementAt(int i) { return strings[i]; }
		});
		jScrollPane3.setViewportView(outputTri);
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		c.fill = GridBagConstraints.HORIZONTAL;

		c.fill = GridBagConstraints.HORIZONTAL;
		c.ipady = 10;      //make this component tall
		c.weightx = 0.0;
		c.gridwidth = 3;
		c.gridx = 0;
		c.gridy = 0;
		add(input, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.ipady = 10;
		c.weightx = 0.5;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 1;
		add(new JLabel("Unigrams",JLabel.CENTER), c);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.ipady = 10;
		c.weightx = 0.5;
		c.gridwidth = 1;
		c.gridx = 1;
		c.gridy = 1;
		add(new JLabel("Bigrams",JLabel.CENTER), c);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.ipady = 10;
		c.weightx = 0.5;
		c.gridwidth = 1;
		c.gridx = 2;
		c.gridy = 1;
		add(new JLabel("Trigrams",JLabel.CENTER), c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.ipady = 100;
		c.weightx = 0.5;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 2;
		add(jScrollPane1, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.ipady = 100;
		c.weightx = 0.5;
		c.gridwidth = 1;
		c.gridx = 1;
		c.gridy = 2;
		add(jScrollPane2, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.ipady = 100;
		c.weightx = 0.5;
		c.gridwidth = 1;
		c.gridx = 2;
		c.gridy = 2;
		add(jScrollPane3, c);

		predictor = wp;
		predictButton = new JButton("Predict");
		menuOutput = new JPopupMenu();

		items = new JMenuItem[11];
		for(int i=0;i<items.length;i++){
			items[i] = new JMenuItem("");
			items[i].addActionListener(this);
			menuOutput.add(items[i]);
		}


		//String[] results = {"cat","cats","cattle","cattles","cattled"};
		predictButton.addActionListener(this);
//		input.addTextListener(this);
		input.getDocument().addDocumentListener(this);
		output.addListSelectionListener(this);
		outputBi.addListSelectionListener(this);
		outputTri.addListSelectionListener(this);
		//main.add(input);
		//main.add(new JLabel(""));
		//main.add(predictButton);
		this.getContentPane().add(main);
	}
	public void cleanup(){
		//predictor.cleanup();
	}


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		GuiLauncher gl = new GuiLauncher(new WordPredictor());
		gl.setDefaultCloseOperation(EXIT_ON_CLOSE);
		gl.setSize(300,225);
		gl.setVisible(true);
	}


	public void actionPerformed(ActionEvent arg0) {
		if(arg0.getSource()==predictButton){
			String beginning = input.getText();
			if(beginning.length()>0){
				String[] results = predictor.getSuggestionsFromDic(input.getText(), 10);
				output.setListData(results);
			}
			else{
				String[] results = {};
				output.setListData(results);
			}
		}
		else if(arg0.getSource().getClass().isInstance(new JMenuItem(""))){
			//System.out.println("Menu item");
			String t = ((JMenuItem)arg0.getSource()).getText();
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
				//menuOutput.setEnabled(false);
			}
		}
	}


//	@Override
//	public void textValueChanged(TextEvent arg0) {
//	if(arg0.getSource()==input){
//	String beginning = input.getText();
//	if(beginning.length()>0 && beginning.charAt(beginning.length()-1)!=' '){
//	jScrollPane1.setVisible(true);
//	String[] text = input.getText().split(" ");
//	String[] results = predictor.getSuggestions(text[text.length-1], 10);
//	/*menuOutput = new JPopupMenu();

//	for(int i=0;i<results.length;i++){
//	items[i].setText(results[i]);
//	menuOutput.add(items[i]);
//	}
//	menuOutput.show(input, input.getX(), input.getY());
//	*/
//	output.setListData(results);
//	input.requestFocus();
//	}
//	else{
//	jScrollPane1.setVisible(false);
//	String[] results = {"Please","Enter","Some","Text","First"};
//	output.setListData(results);
//	}
//	}
//	}

	boolean clicked=true;
	public void valueChanged(ListSelectionEvent arg0) {
		if(arg0.getSource().getClass().isInstance(output) && clicked){
			String t = (String)((JList)arg0.getSource()).getSelectedValue();
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
				//input.setCaretPosition(input.getText().length());
			}
		}		
	}


	public void keyPressed(KeyEvent arg0) {
		//System.out.println(arg0.getKeyCode());
		if(arg0.getKeyCode()==40){
			input.getKeymap();
			System.out.println(output.getModel().getSize());
			if(output.getSelectedIndex()<output.getModel().getSize()){
				clicked=false;
				output.setSelectedIndex(output.getSelectedIndex()+1);
			}
			clicked=true;
		}
		else if(arg0.getKeyCode()==38){
			//System.out.println(output.getModel().getSize());
			if(output.getSelectedIndex()>0){
				clicked=false;
				output.setSelectedIndex(output.getSelectedIndex()-1);
				System.out.println(output.getBounds());
				System.out.println(output.getCellBounds(output.getSelectedIndex(), output.getSelectedIndex()));
				jScrollPane1.scrollRectToVisible(output.getCellBounds(output.getSelectedIndex(), output.getSelectedIndex()));
			}
			clicked=true;
		}
		else if(arg0.getKeyCode()==32){
			String t = (String)output.getSelectedValue();
			if(t!=null){
				int j = input.getText().lastIndexOf(" ");
				if(j==-1){
					input.setText(t);
				}
				else{
					String beginning = input.getText().substring(0,j);
					input.setText(beginning+" "+t);
				}
				input.requestFocus();
				//input.setCaretPosition(input.getText().length());
			}
		}
		else if(arg0.getKeyCode()==10){
			input.setText("");
		}
	}


	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}


	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}


	public void changedUpdate(DocumentEvent arg0) {

	}


	public void insertUpdate(DocumentEvent arg0) {
		updateList();
	}


	public void removeUpdate(DocumentEvent arg0) {
		updateList();
	}
	public void updateList(){
		String beginning = input.getText();
		if(beginning.length()>0){
			jScrollPane1.setVisible(true);
			String[] text;
			boolean unigram=true;
			if(beginning.charAt(beginning.length()-1)==' '){
				String t = beginning;
				unigram=false;
				//t+="z";
				text = t.toLowerCase().split(" ");
			}
			else
				text = beginning.toLowerCase().split(" ");
			System.out.println("Last char"+input.getText().charAt(input.getText().length()-1));
			String[][] results = new String[3][5];
			int startIndex=text.length-3;
			if(startIndex<0) startIndex=0;
			int j=0;
			for(int i=startIndex;i<text.length;i++){
				//System.out.println("text.length="+text.length+" i="+i);
				String[] outputdata = new String[text.length-i];
				System.arraycopy(text, i, outputdata, 0, text.length-i);
				//outputdata = Arrays.copyOfRange(text, i, text.length);  Apparently this doesn't work in Java 1.5

				//System.out.println("i="+i+" j="+j+" "+outputdata[0]+" "+outputdata.length);
				System.out.println("Sending...");
				for(String s:outputdata)
					System.out.println(s);
				results[j++] = predictor.getSuggestionsGramBased(outputdata, 5);				
				System.out.println((3-j)+"gram Results: "+results[j-1].length);
				for(int a=0;a<results[j-1].length;a++){
					System.out.println(results[j-1][a]);
				}
			}
			output.setListData(results[0]);
			String[] empty = new String[5];
			System.out.println("j="+j);
			output.setListData(empty);
			outputBi.setListData(empty);
			outputTri.setListData(empty);
			if(unigram)
				if(j==1){
					output.setListData(results[0]);
					outputBi.setListData(empty);
					outputTri.setListData(empty);
				}
				else if(j==2){
					output.setListData(results[1]);
					outputBi.setListData(results[0]);
					outputTri.setListData(empty);
				}
				else if(j==3){
					output.setListData(results[2]);
					outputBi.setListData(results[1]);
					outputTri.setListData(results[0]);
				}
			input.requestFocus();
		}
		else{
			//jScrollPane1.setVisible(false);
			String[] results = {};
			output.setListData(results);
		}
	}

}
