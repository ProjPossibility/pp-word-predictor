package org.ss12.wordprediction;

public class WordReader
{

	/**
	 * @param args
	 */
	public void nextWords(String[] nw)
	{
		int len = nw.length;
		String last1 = null;
		String last2 = null;
		
		for(int i = 0; i < len; i++)
		{	
			if(i == 0 && last2 != null)
				addBigram(last1, nw[0]);
			if(i == 0 && last1 != null && last2 != null)
				addTrigram(last1, last2, nw[0]);
			
			
		}
	}
}