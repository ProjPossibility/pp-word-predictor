package org.ss12.wordprediction;

public class WordReader
{
	private String last1;
	private String last2;
	private WordPredictor wp;
	
	public WordReader(WordPredictor wp)
	{
		this.wp = wp;
		last1 = null;
		last2 = null;
	}
	public void nextWords(String[] nw)
	{
		int len = nw.length;
		
		for(int i = 0; i < len; i++)
		{	
			if(i == 0 && last2 != null)
				wp.addBigram(last1, nw[0]);
			
			if(i == 0 && last1 != null && last2 != null)
				wp.addTrigram(last1, last2, nw[0]);
			
			if(i > 0)
			{
				wp.addBigram(nw[i-1], nw[i]);
				
				if(i == 1 && last2 != null)
					wp.addTrigram(last2, nw[i-1], nw[i]);
				else
					wp.addTrigram(nw[i-2], nw[i-1], nw[i]);
			}
			
		}
		last1 = nw[i-1];
		last2 = nw[i];
	}
}