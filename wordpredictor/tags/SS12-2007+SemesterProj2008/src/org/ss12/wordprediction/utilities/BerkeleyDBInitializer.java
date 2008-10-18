/*-
 * See the file LICENSE for redistribution information.
 *
 * Copyright (c) 2002,2008 Oracle.  All rights reserved.
 *
 * $Id: HelloDatabaseWorld.java,v 1.23.2.2 2008/01/07 15:13:59 cwl Exp $
 */

package org.ss12.wordprediction.utilities;

import java.io.File;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;

import org.ss12.wordprediction.reader.ImportLauncher;

import com.sleepycat.bind.serial.ClassCatalog;
import com.sleepycat.bind.serial.StoredClassCatalog;
import com.sleepycat.bind.tuple.TupleBinding;
import com.sleepycat.collections.StoredSortedMap;
import com.sleepycat.collections.TransactionRunner;
import com.sleepycat.collections.TransactionWorker;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;

public class BerkeleyDBInitializer implements TransactionWorker {

    private static final String[] INT_NAMES = {
        "Hello", "Database", "World",
    };
    private static boolean create = true;

    private Environment env;
    private ClassCatalog catalog;
    private Database db;
    private SortedMap map;

    /** Creates the environment and runs a transaction */
    public static void main(String args[])
        throws Exception {

        String dir = "./tmp";

        // environment is transactional
        EnvironmentConfig envConfig = new EnvironmentConfig();
        envConfig.setTransactional(true);
        if (create) {
            envConfig.setAllowCreate(true);
        }
        Environment env = new Environment(new File(dir), envConfig);

        // create the application and run a transaction
        BerkeleyDBInitializer worker = new BerkeleyDBInitializer(env);
        TransactionRunner runner = new TransactionRunner(env);
        try {
            // open and access the database within a transaction
            runner.run(worker);
        } finally {
            // close the database outside the transaction
            worker.close();
        }
    }

    /** Creates the database for this application */
    private BerkeleyDBInitializer(Environment env)
        throws Exception {

        this.env = env;
        open();
    }

    /** Performs work within a transaction. */
    public void doWork()
        throws Exception {

        writeAndRead();
        ImportLauncher.main(new String[0]);
        writeAndRead();
    }

    /** Opens the database and creates the Map. */
    private void open()
        throws Exception {

        // use a generic database configuration
        DatabaseConfig dbConfig = new DatabaseConfig();
        dbConfig.setTransactional(true);
        if (create) {
            dbConfig.setAllowCreate(true);
        }

        // catalog is needed for serial bindings (java serialization)
        Database catalogDb = env.openDatabase(null, "catalog", dbConfig);
        catalog = new StoredClassCatalog(catalogDb);

        // use Integer tuple binding for key entries
        TupleBinding keyBinding = TupleBinding.getPrimitiveBinding(Long.class);
        TupleBinding dataBinding = TupleBinding.getPrimitiveBinding(Float.class);
//        TupleBinding dataBinding =
//            TupleBinding.getPrimitiveBinding(Integer.class);

        // use String serial binding for data entries
//        SerialBinding keyBinding = new SerialBinding(catalog, String.class);

        this.db = env.openDatabase(null, "helloworld", dbConfig);

        // create a map view of the database
        this.map = new StoredSortedMap(db, keyBinding, dataBinding, true);
    }

    /** Closes the database. */
    private void close()
        throws Exception {

        if (catalog != null) {
            catalog.close();
            catalog = null;
        }
        if (db != null) {
            db.close();
            db = null;
        }
        if (env != null) {
            env.close();
            env = null;
        }
    }

    /** Writes and reads the database via the Map. */
    private void writeAndRead() {

        // check for existing data
        String val = null;//(String) map.get(new byte[]);
        if (val == null) {
            System.out.println("Writing data");
            // write in reverse order to show that keys are sorted
            for (int i = INT_NAMES.length - 1; i >= 0; i -= 1) {
                map.put(new Integer(i),new Float(i/2.0));
            }
        }
        // get iterator over map entries
        Iterator iter = map.entrySet().iterator();
        System.out.println("Reading data");
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            System.out.println(entry.getKey().toString() + ' ' +
                               entry.getValue());
        }
    }
}
