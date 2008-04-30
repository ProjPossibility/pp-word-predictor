package org.ss12.wordprediction.customer;

import junit.framework.TestCase;

/**
 * unit testing for NewPredictionModel
 * 
 * @author Lily 
 */

public class NewPredictionModelTest extends TestCase {
	public void testSortList(){
		NewPredictionModel pm = new NewPredictionModel();
		pm.getUniScoreMap().put("a", 1.0);
		pm.getUniScoreMap().put("b", 2.0);
		pm.getUniScoreMap().put("c", 3.0);
		System.out.println(pm.getUniScoreMap());
		pm.getUniScoreMap().put("a", 1.0);
		pm.getUniScoreMap().put("b", 2.0);
		pm.getUniScoreMap().put("c", 3.0);
		System.out.println(pm.getUniScoreMap());
		pm.sortList(pm.getUniScoreMap());
		System.out.println(pm.getUniScoreMap());
	}
	
	public void testTriScorer(){
		NewPredictionModel pm = new NewPredictionModel();
		
	}
}
