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
		pm.getuniScoreMap().put("a", 1.0);
		pm.getuniScoreMap().put("b", 2.0);
		pm.getuniScoreMap().put("c", 3.0);
		System.out.println(pm.getuniScoreMap());
		//pm.sortList(pm.getuniScoreMap());
		//System.out.println(pm.getuniScoreMap());
	}
}
