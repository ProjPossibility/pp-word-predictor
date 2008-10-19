import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.TreeSet;

import org.apache.commons.codec.language.DoubleMetaphone;




public class SimilarWord {
	
	private HashMap<String, List<String>> metaphoneList; //this contains the list of metaphones
	
	
	public static void main(String[] args) {
		SimilarWord sw = new SimilarWord();
		
		sw.CompileMetaphoneList("wordlist\\wordsEn-109582.txt", "wordlist\\DoubleMetaphone.csv");
		sw.LoadMetaphoneList("wordlist\\DoubleMetaphone.csv");
		sw.WriteMetaphoneList("wordlist\\DoubleMetaphone-DEBUG.csv");
		System.out.println(sw.getSimilarWords("test"));
	}
	
	public List<String> getSimilarWords(String theword) {
		
		// get the metaphone
		DoubleMetaphone dm = new DoubleMetaphone();
		
		// uses the hashmap that should be already initialized
		return metaphoneList.get(dm.doubleMetaphone(theword));
	}
	
	/**
	 * The creates the Metaphone list given an input file
	 * This auto loads the values into the HashMap
	 * 
	 * @param infile
	 * @param outfile
	 * @return
	 * true if it is successful, false otherwise
	 */
	public boolean CompileMetaphoneList(String infile, String outfile) {
		// this file takes an input flat file one-line-per-word
		// and outputs an outfile in the format of .. CSV
		//
		// each line has ... a sorted phonteic,word,word,...
		DoubleMetaphone dm;
		dm = new DoubleMetaphone();
		
		metaphoneList = new HashMap<String, List<String>>();
		
		// open infile for read
		File fin = new File(infile);
		
	    try {
			Scanner s = new Scanner(fin);
			
			// set delmiters ONLY to the linux/windows line endings, spaces excluded (words may have spaces)
			s.useDelimiter("[\\r\\n]+");

	    	
	    	// while more tokens left (more words left in the file)
	    	while (s.hasNext()) {
	    		String theword = s.next();
	    		
				// for each word in infile, build HashMap for all possible DoubleMetaphones
		    	String dmkey = dm.doubleMetaphone(theword);
		    	
		    	//System.out.println(theword + " - " + dmword);
		    	
		    	// hashmap using the double metaphone as key
		    	
		    	// list is generic to String, and it's an interface to the LinkedList uses later
		    	List<String> dmlist = metaphoneList.get(dmkey);
		    	
		    	if (dmlist == null) {
		    		// if dmlist is not defined, it doesn't exist yet, create one
		    		dmlist = new LinkedList<String>(); //create empty linked list
		    	}
		    	
		    	// append the word to the list (but make sure it's sorted)
		    	TreeSet<String> mergedSet = new TreeSet<String>();
		    	mergedSet.addAll(dmlist);
		    	mergedSet.add(theword);
		    	
		    	metaphoneList.put(dmkey, new ArrayList<String>(mergedSet)); // put replacing old list
	    	}
	    	
	    	s.close(); // close the input file
	    	

	    	
			// open outfile for write, truncating whatever is already there
			// dump HashMap to the format we want in outfile
	    	return WriteMetaphoneList(outfile);
 
	    } catch (Exception e) {
    		e.printStackTrace();
    		return false;
    	}
	}
	
	/**
	 * dumps hashmap
	 * @param outfile
	 * @return
	 */
	public boolean WriteMetaphoneList(String outfile) {
		// write to metaphone list (dump hashmap)
		try {
	    	File out = new File (outfile);
	    	// out.createNewFile(); //create new file if one doesn't exit
	    	FileWriter outwriter = new FileWriter(out, false);  // truncate existing
	    	
	    	// for each key in the hashmap
			for (Iterator<String> i = metaphoneList.keySet().iterator(); i.hasNext(); ) {
				// dump
				String hkey = i.next();
				outwriter.write(hkey + ",");
				List<String> lkey = metaphoneList.get(hkey); 
				
				//lkey must have at least one element
				Iterator<String> keylist = lkey.iterator();
				do {
					outwriter.write(keylist.next());
					
					if (keylist.hasNext()) 
						outwriter.write(",");
				} while(keylist.hasNext());
				
				outwriter.write("\n");
			}
			
			outwriter.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	
	/**
	 * this loads the selected list into a hashmap.  You can load multiple file lists
	 * @param infile
	 * @return
	 * true if it succeeds in load, false if error occurred
	 */
	public boolean LoadMetaphoneList(String infile) {

		try {
			// loads the infile that was compiled using the CompileMetaphoneList() function
			File fin = new File(infile);
			
			Scanner s = new Scanner(fin);
			
			// set delmiters ONLY to the linux/windows line endings, spaces excluded (words may have spaces)
			s.useDelimiter("[\\r\\n]+");
			
			
			while (s.hasNext())
			{
				String[] line = s.next().split(","); //it's a CSV, split by commas
				
				//should have at least 2 indexes, throw exception if not correct format
				if (line.length < 2)
					throw new Exception("Invalid Metaphone list format");
				
				// create a list of strings that start at the 2nd element
				List<String> filedm = Arrays.asList(line);
				filedm = filedm.subList(1, filedm.size());
				// list.set(0, "foo"); ... setting will change the original line!
				// System.out.println(Arrays.toString(line));

		    	List<String> dmlist = metaphoneList.get(line[0]);
		    	
		    	if (dmlist == null) {
		    		// if Metaphone list is not defined, it doesn't exist yet, create one
		    		dmlist = new LinkedList<String>(); //create empty linked list
		    	}
		    	
		    	// append the word to the list (no dupes)
		    	//
		    	// create a hashset that cannot contain dupes and merge the two lists
		    	// loads the list SORTED
		    	TreeSet<String> mergedSet = new TreeSet<String>();
		    	mergedSet.addAll(dmlist);
		    	mergedSet.addAll(filedm);
		    	
		    	metaphoneList.put(line[0], new ArrayList<String>(mergedSet)); // put replacing old list

			}
	    } catch (Exception e) {
    		e.printStackTrace();
    		return false;
    	}

	    return true; //if successul load
	}
}
