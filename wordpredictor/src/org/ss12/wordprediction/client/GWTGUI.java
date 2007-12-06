package org.ss12.wordprediction.client;


import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class GWTGUI implements EntryPoint {

	private final class NgramCallback implements AsyncCallback {
		int instance;
		public NgramCallback(int length) {
			instance=length-1;
		}

		public void onFailure(Throwable caught) {
		      // TODO Auto-generated method stub
		      
		 }

		public void onSuccess(Object result) {
		      String[] suggestions = (String[]) result;
				//String printedSuggestions = new String();
				for(int i=0;i<min(suggestions.length,label[instance].length);i++){
					//printedSuggestions+=suggestions[i]+"<br />";
					label[instance][i].setText(suggestions[i]);
				}
				if(textbox.isReadOnly()){
					textbox.setText("");
					textbox.setReadOnly(false);
					textbox.setEnabled(true);
				}
				//label.setText(printedSuggestions);
		 }

		private int min(int length, int length2) {
			if(length>length2)
				return length2;
			else
				return length;
		}
	}
	/**
	 * This is the entry point method.
	 */
    private PredictionServiceAsync getPredictionService(){
        PredictionServiceAsync predictionService = (PredictionServiceAsync) 
		             GWT.create(PredictionService.class);
        ServiceDefTarget endpoint = (ServiceDefTarget) predictionService;
        String moduleRelativeUrl = GWT.getModuleBaseURL() + "predictionService";
        endpoint.setServiceEntryPoint(moduleRelativeUrl);
        return predictionService;
   }
    public void getPredictions(String[] text, int numPredictions){
    	for(int i=0;i<text.length;i++)
    		System.out.println("Word: "+text[i]);
        PredictionServiceAsync predictionService = this.getPredictionService();
        AsyncCallback callback = new NgramCallback(text.length);
        predictionService.getSuggestionsGramBased(text, numPredictions, callback);
   }
	private TextBox textbox = new TextBox();
	private Label label[][] = new Label[3][5];
	public void onModuleLoad() {
		//pm = new WordPredictor();
		textbox.setText("Loading...");
		textbox.setWidth("40em");
		textbox.setReadOnly(true);
		textbox.setEnabled(false);
		textbox.addKeyboardListener(new KeyboardListener(){

			public void onKeyDown(Widget sender, char keyCode, int modifiers) {
				// TODO Auto-generated method stub
				
			}

			public void onKeyPress(Widget sender, char keyCode, int modifiers) {
				// TODO Auto-generated method stub
				
			}

			public void onKeyUp(Widget sender, char keyCode, int modifiers) {
				// TODO Auto-generated method stub
				handleChange();
			}
			
		});

		// Assume that the host HTML has elements defined whose
		// IDs are "slot1", "slot2".  In a real app, you probably would not want
		// to hard-code IDs.  Instead, you could, for example, search for all 
		// elements with a particular CSS class and replace them with widgets.
		//
		RootPanel.get("top").add(textbox);
		for(int j=0;j<label.length;j++){
			for(int i=0;i<label[j].length;i++){
				label[j][i] = new Label();
				RootPanel.get("word"+(j+1)+""+(i+1)).add(label[j][i]);
			}
		}
		getPredictions("".split(" "),5);
	}
	protected void handleChange() {
		String[] text = textbox.getText().toLowerCase().split(" ");
		int startIndex=text.length-3;
		if(startIndex<0) startIndex=0;
		for(int i=startIndex;i<text.length;i++){
			String[] outputdata = new String[text.length-i];
			for(int j=i;j<text.length;j++){
				outputdata[j-i]=text[j];
			}
			getPredictions(outputdata, 5);				
		}
	}
}
