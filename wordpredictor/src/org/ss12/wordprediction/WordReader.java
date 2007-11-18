package org.ss12.wordprediction;

import org.ss12.wordprediction.model.PredictionModel;

public class WordReader
{
	private String prePreWord;
	private String preWord;
	private final PredictionModel wp;
	boolean endSentence;
	public WordReader(PredictionModel wp)
	{
		this.wp = wp;
		prePreWord = null;
		preWord = null;
		endSentence=false;
	}
	
	public void nextWords(String[] nw)
	{	
		for(int i = 0; i < nw.length; i++)
		{	
			endSentence=false;
			nw[i]=nw[i].toLowerCase();
			if((nw[i]=wordify(nw[i])).equals("")){
				prePreWord = preWord= null;
				continue;
			}
			System.out.print(nw[i]+" ");
			wp.addUnigram(nw[i]);
			
			if(preWord != null)
				wp.addBigram(preWord, nw[i]);
			
			if(prePreWord != null && preWord != null)
				wp.addTrigram(prePreWord, preWord, nw[i]);
			
			if(!endSentence){
				prePreWord = preWord;
				preWord = nw[i];
			}
			else{
				prePreWord=preWord=null;
				System.out.println();
			}
		}
	}
	public String wordify(String word){
		word.trim();
		for(int i=0;i<word.length()-1;i++){
			char c = word.charAt(i);
			if((c>='a' && c<='z')||(c>='A' && c<='Z')||(c=='\'')||(c=='-')){
				continue;
			}
			else
				return "";
		}
		if(word.length()>0){
			char c = word.charAt(word.length()-1);
			if(!(
				(c>='a' && c<='z')
					||
				(c>='A' && c<='Z')
					||
				(c=='\'')
					||
				(c=='-')
				)){
				word = word.substring(0, word.length()-1);
				endSentence=true;
			}
		}
		return word;
	}
}