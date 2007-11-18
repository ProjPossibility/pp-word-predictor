package org.ss12.wordprediction.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JFrame;
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

import org.ss12.wordprediction.WordLoader;
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
            String[] strings = {};
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(output);

        outputBi.setModel(new javax.swing.AbstractListModel() {
            String[] strings = {};
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane2.setViewportView(outputBi);

        outputTri.setModel(new javax.swing.AbstractListModel() {
            String[] strings = {};
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane3.setViewportView(outputTri);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(main);
        main.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(51, 51, 51)
                        .addComponent(input, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane3)))
                .addContainerGap(158, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(input, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(115, Short.MAX_VALUE))
        );

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
		//main.add(input);
		//main.add(new JLabel(""));
		//main.add(predictButton);
		this.getContentPane().add(main);
	}
	public void cleanup(){
		predictor.cleanup();
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
		File uni,bi,tri;
		uni = new File("resources/dictionaries/user/uni.dat");
		bi = new File("resources/dictionaries/user/bi.dat");
		tri = new File("resources/dictionaries/user/tri.dat");
		PredictionModel wp;
		wp = new WordPredictor(wl.getWords(),wl.loadNgram(uni),wl.loadNgram(bi),wl.loadNgram(tri));
		
		GuiLauncher gl = new GuiLauncher(wp);
		gl.setDefaultCloseOperation(EXIT_ON_CLOSE);
		gl.setSize(300,200);
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
//		if(arg0.getSource()==input){
//			String beginning = input.getText();
//			if(beginning.length()>0 && beginning.charAt(beginning.length()-1)!=' '){
//				jScrollPane1.setVisible(true);
//				String[] text = input.getText().split(" ");
//				String[] results = predictor.getSuggestions(text[text.length-1], 10);
//				/*menuOutput = new JPopupMenu();
//				
//				for(int i=0;i<results.length;i++){
//					items[i].setText(results[i]);
//					menuOutput.add(items[i]);
//				}
//				menuOutput.show(input, input.getX(), input.getY());
//				*/
//				output.setListData(results);
//				input.requestFocus();
//			}
//			else{
//				jScrollPane1.setVisible(false);
//				String[] results = {"Please","Enter","Some","Text","First"};
//				output.setListData(results);
//			}
//		}
//	}

	boolean clicked=true;
	@Override
	public void valueChanged(ListSelectionEvent arg0) {
		if(arg0.getSource()==output && clicked){
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


	@Override
	public void keyPressed(KeyEvent arg0) {
		System.out.println(arg0.getKeyCode());
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
			System.out.println(output.getModel().getSize());
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
				input.setCaretPosition(input.getText().length());
			}
		}
	}


	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void changedUpdate(DocumentEvent arg0) {

	}


	@Override
	public void insertUpdate(DocumentEvent arg0) {
		updateList();
	}


	@Override
	public void removeUpdate(DocumentEvent arg0) {
		updateList();
	}
	public void updateList(){
		String beginning = input.getText();
		if(beginning.length()>0){
			jScrollPane1.setVisible(true);
			String[] text = input.getText().split(" ");
			String[][] results = new String[3][5];
			int startIndex=text.length-3;
			if(startIndex<0) startIndex=0;
			int j=0;
			for(int i=startIndex;i<text.length;i++){
				String[] outputdata = new String[5];
				outputdata = Arrays.copyOfRange(text, i, text.length);
				results[j++] = predictor.getSuggestionsGramBased(outputdata, 5);				
			}
			
			output.setListData(results[0]);
			String[] empty = new String[5];
			if(j>1)
				outputBi.setListData(results[1]);
			else
				outputBi.setListData(empty);
			if(j>2)
				outputTri.setListData(results[2]);
			else
				outputTri.setListData(empty);
			input.requestFocus();
		}
		else{
			jScrollPane1.setVisible(false);
			String[] results = {};
			output.setListData(results);
		}
	}

}
