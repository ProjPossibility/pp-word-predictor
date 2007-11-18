package org.ss12.wordprediction;

import org.ss12.wordprediction.model.PredictionModel;

public class WordReader
{
	private String prePreWord;
	private String preWord;
	private PredictionModel wp;
	public WordReader(PredictionModel wp)
	{
		this.wp = wp;
		prePreWord = null;
		preWord = null;
	}
	
	public void nextWords(String[] nw)
	{	
		for(int i = 0; i < nw.length; i++)
		{	
			nw[i]=nw[i].toLowerCase();
			if((nw[i]=isWord(nw[i])).equals(""))
				continue;
			wp.addUnigram(nw[i]);
			
			if(preWord != null)
				wp.addBigram(preWord, nw[i]);
			
			if(prePreWord != null && preWord != null)
				wp.addTrigram(prePreWord, preWord, nw[i]);
			
			prePreWord = preWord;
			preWord = nw[i];
		}
	}
	public String isWord(String word){
		word.trim();
		for(int i=0;i<word.length()-1;i++){
			char c = word.charAt(i);
			if((c>='a' && c<='z')||(c>='A' && c<='Z')){
				continue;
			}
			else
				return "";
		}
		char c = word.charAt(word.length()-1);
		if(!((c>='a' && c<='z')||(c>='A' && c<='Z'))){
			word = word.substring(0, word.length()-1);
		}
		return word;
	}
}