package org.ss12.wordprediction.newcore;

import java.io.File;

import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;

/**
 * Unit test for {@link BDBCustomLexicon}.
 */
public class BDBCustomLexiconTest extends CustomLexiconTest {
	protected CustomLexicon<MockAnnotation> getCustomLexicon() throws Exception {

		EnvironmentConfig envConfig = new EnvironmentConfig();
		envConfig.setTransactional(true);
		envConfig.setAllowCreate(true);
		Environment myEnv = new Environment(new File("./resources/dictionaries/bdb/test"), envConfig);
		try {
			myEnv.removeDatabase(null, BDBCustomLexicon.UNIGRAM_DB_NAME);
			myEnv.removeDatabase(null, BDBCustomLexicon.BIGRAM_DB_NAME);
			myEnv.removeDatabase(null, BDBCustomLexicon.TRIGRAM_DB_NAME);
			myEnv.removeDatabase(null, "classDb");
		} catch (DatabaseException e) {}
		
		return new BDBCustomLexicon<MockAnnotation>(myEnv, new MockAnnotationFactory(), MockAnnotation.class);
	}
}
