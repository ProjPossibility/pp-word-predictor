import java.io.*;
import java.net.URL;
import java.util.*;

/*
 * Class handles parsing of Wikimedia-formatted pages to plain  text.
 */
public class WikiProcessor {

	/*
	 * Internal method used to get a page from wikipedia via http.
	 * 
	 * @param url The page to get the text of
	 */
	private String getPage(String url) {
		StringBuffer rtn = new StringBuffer();
		try {
			URL u = new URL(url);
			BufferedReader in = new BufferedReader(new InputStreamReader(u.openStream()));
			String line = "";
			while((line = in.readLine())!=null)
				rtn.append(line + "\n");
			return rtn.toString();
		}
		catch(Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	
	/*
	 * Internal method to save text to a file.
	 * 
	 * @param file The file to save text to
	 * @param text The text to write to the file
	 */
	private void saveToFile(String file, String text) {
		try {
			PrintWriter out = new PrintWriter(new FileWriter(file));
			out.print(text);
			out.flush();
			out.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * Key method to remove the wikimedia format. Returns the text of the page
	 * minus the wikimedia markup.
	 * 
	 * @param text The text of the page with the markup
	 * @return The plain text of the page
	 */
	private String removeWikiFormat(String text) {
		text = isolateTagContent("text", text);
		text = text.replaceAll("=", "");
		text = text.replaceAll("\'", "");

		//remove lines with image tags
		while(text.indexOf("[[Image") > -1) {
			int theIndex = text.indexOf("[[Image");
			int nextNewLine = text.indexOf("\n", theIndex);
			text = text.substring(0, theIndex) + text.substring(nextNewLine+1);
		}

		//remove {{ tags
		while(text.indexOf("{{") > -1) {
			text = removeTagContent("\\{\\{", "}}", text);
		}

		//handle bracket terms
		while(text.indexOf("[[") > -1) {
			int beginIndex = text.indexOf("[[");
			int endIndex = text.indexOf("]]", beginIndex);
			System.out.println("BI: "  + beginIndex);
			System.out.println("EI: " + endIndex);
			String substr = text.substring(beginIndex,(endIndex+2));
			String term = processBracketTerm(substr);
			text = text.substring(0, beginIndex) + term + text.substring(endIndex+2, text.length());
		}

		//remove bullets and lone []
		text = text.replaceAll("\\*", "");
		text = text.replaceAll("\\[", "");
		text = text.replaceAll("]", "");


		//remove &words, links
		String[] words = text.split(" ");
		StringBuffer rtn = new StringBuffer();
		for(int x=0; x < words.length; x++) {
			if(words[x].indexOf("&")==-1 && !words[x].startsWith("http://"))
				rtn.append(words[x] + " ");
		}
		text = rtn.toString();



		return text;
	}

	/*
	 * Internal method used to extract the text from a given html tag.
	 * 
	 * @param tag The name of the tag
	 * @param text The text of the page
	 * @return The tag content
	 */
	private String isolateTagContent(String tag, String text) {
		int openBeginIndex = text.indexOf("<" + tag);
		int openCloseIndex = text.indexOf(">", openBeginIndex);
		int closeBeginIndex = text.indexOf("</" + tag);
		int closeEndIndex = text.indexOf(">", closeBeginIndex);
		return text.substring(openCloseIndex+1, closeBeginIndex);
	}
	
	/*
	 * Function to process a bracket term in wikimedia formating
	 * 
	 * @param bracketTerm The bracket term
	 * @return The bracket term after processing
	 */
	private String processBracketTerm(String bracketTerm) {
		if(bracketTerm.indexOf("|") > -1) {
			int pipeIndex = bracketTerm.indexOf("|");
			String rtn = bracketTerm.substring(pipeIndex+1);
			rtn = rtn.replaceAll("]]", "");
			return rtn;
		}
		else if(bracketTerm.indexOf(":") > -1) {
			return "";
		}
		else {
			return bracketTerm.substring(2,bracketTerm.length()-2);
		}
	}
	
	/*
	 * Removes the content of a tag. This method handles nested tags of the same type.
	 */
	private String removeTagContent(String openTag, String closeTag, String text) {
		char open = (char)8;
		char close = (char) 244;
		int numCloseCount = 0;

		String rtnText = text.replaceAll(openTag,"" + open);
		rtnText = rtnText.replaceAll(closeTag, "" + close);

		Stack<Integer> s = new Stack<Integer>();

		for(int x=0; x < rtnText.length(); x++) {
			char c = rtnText.charAt(x);
			if(c==open) {
				s.push(x);
				System.out.println(x);
			}
			else if(c==close) {
				int openIndex = s.pop();
				int closeIndex  = x;
				numCloseCount++;

				if(s.isEmpty()) {
					return text.substring(0, openIndex) + text.substring(closeIndex + closeTag.length()*numCloseCount  + 1, text.length());
				}
			}
		}

		return text;
	}

	/*
	 * Method call to use to convert a subset of the wikipedia articles  specified to plain
	 * text. This method will save the output of each article processed to a text file.
	 */
	public void convertFile(String wikiDB, int articleStart, int articleEnd) {

		//get to start index in wikiDB giant file
		int index=0;
		int numToStart = 0;
		while(wikiDb.indexOf("<text>", index) > -1 && numToStart < articleStart) {
			index = wikiDb.indexOf("<text>", index);
			index++;
			numToStart++;
		}

		//process articles
		int numArticlesProcessed = 0;
		while(wikiDb.indexOf("<text>", index) > -1 && numArticlesProcessed < articleEnd) {
			index = wikiDb.indexOf("<text>", index);
			String noFormatting = removeWikiFormating(wikiDB.substring(index));
			saveToFile("article" + numArticlesProcessed + ".txt", noFormatting);
			index++;
			numArticlesProcessed++;
		}
	}
		
	public static void main(String[] args) {
		WikiProcessor w = new WikiProcessor();
		String text = w.getPage("http://en.wikipedia.org/wiki/Special:Export/Fallacy");
		w.saveToFile("test.txt", text);
		text = w.removeWikiFormat(text);
		w.saveToFile("test1.txt", text);
	}
}
