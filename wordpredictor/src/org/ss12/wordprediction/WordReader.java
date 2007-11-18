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
			wp.addUnigram(nw[i]);
			
			if(preWord != null)
				wp.addBigram(preWord, nw[i]);
			
			if(prePreWord != null && preWord != null)
				wp.addTrigram(prePreWord, preWord, nw[i]);
			
			prePreWord = preWord;
			preWord = nw[i];
		}
	}
}