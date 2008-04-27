package org.ss12.wordprediction.wikiprocessor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

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
		if(text.length()<100)
			return;
		try {
			PrintWriter out = new PrintWriter(new FileWriter("resources/articles/"+file));
			out.print(text);
			out.flush();
			out.close();
			System.out.println("Done saving "+file);
			//System.out.println(text);
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
		if(text.length()<150)
			return "";
		text = isolateTagContent("text", text);
		text = text.replaceAll("=", "");
		text = text.replaceAll("\'\'\'", "");
		text = text.replaceAll("\'\'", "");

		//remove lines with image tags
		while(text.indexOf("[[Image") > -1) {
			int theIndex = text.indexOf("[[Image");
			int nextNewLine = text.indexOf("\n", theIndex);
			text = text.substring(0, theIndex) + text.substring(nextNewLine+1);
		}

		//remove {{ tags
		while(text.indexOf("{{") > -1 && text.indexOf("}}") > -1) {
			text = removeTagContent("\\{\\{", "}}", text);
		}

		//handle bracket terms
		int beginIndex=-1;
		int endIndex=-1;
		while((beginIndex = text.indexOf("[[")) > -1 && (endIndex = text.indexOf("]]",beginIndex)) > -1) {
//			System.out.println("BI: "  + beginIndex);
//			System.out.println("EI: " + endIndex);
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
		char close = (char) 27;
		int numCloseCount = 0;

		String rtnText = text.replaceAll(openTag,"" + open);
		rtnText = rtnText.replaceAll(closeTag, "" + close);

		Stack<Integer> s = new Stack<Integer>();
		int openIndex=-1;
		int closeIndex=-1;
		
		for(int x=0; x < rtnText.length(); x++) {
			char c = rtnText.charAt(x);
			if(c==open) {
				s.push(x);
				//System.out.println(x);
			}
			else if(c==close && !s.isEmpty()) {
				openIndex = s.pop();
				closeIndex  = x;
				numCloseCount++;

				//if(s.isEmpty()) {
				return text.substring(0, openIndex) + text.substring(closeIndex + closeTag.length()*numCloseCount  + 1, text.length());
				//}
			}
		}
//		if(openIndex>=0){
//			return text.substring(0, openIndex) + text.substring(closeIndex + closeTag.length()*numCloseCount  + 1, text.length());
//		}
		return text.replaceAll(openTag, "").replaceAll(closeTag, "");
	}

	/*
	 * Method call to use to convert a subset of the wikipedia articles  specified to plain
	 * text. This method will save the output of each article processed to a text file.
	 * Processes articles in the range [articleStart, articleEnd)
	 * 
	 * @param wikiDB The wikiDB file in XML format
	 * @param articleStart The article to start at in the XML file, inclusively
	 * @param articleEnd The article to end at in the XML file, exclusively
	 * 
	 */
	public void convertFile(File wikiDB, int articleStart, int articleEnd) throws IOException {
		System.out.println("Start converting articles [" + articleStart + " - " + articleEnd+")");
		long time = System.currentTimeMillis();
		//get to start index in wikiDB giant file
		BufferedReader br = new BufferedReader(new FileReader(wikiDB));
		int numToStart = 0;
		String line = "";
		while(br.ready() && numToStart <= articleStart){
			line = br.readLine();
			if(line.contains("<text "))
				numToStart++;
		}
		int maxThreads=10;
		BlockingQueue<Runnable> q = new ArrayBlockingQueue<Runnable>(50);
		ThreadPoolExecutor e = new ThreadPoolExecutor(maxThreads,maxThreads,0,TimeUnit.SECONDS,q,new ThreadPoolExecutor.CallerRunsPolicy());

		//process articles
		int numArticlesProcessed = 0;
		StringBuffer article = new StringBuffer();
		while(br.ready() && (numArticlesProcessed+articleStart) < articleEnd){
			article.append(line+"\n");
			if(line.contains("</text>")){
				String filename = ""+(numArticlesProcessed+articleStart);
				for(int i=filename.length();i<8;i++)
					filename = "0"+filename;
				filename = "article"+filename+".txt";

				e.execute(new ArticleWorkUnit(article.toString(),filename));
				numArticlesProcessed++;
				article = new StringBuffer();
			}
			line = br.readLine();
		}
		Thread.yield();
		Thread.yield();
		List<Runnable> l = e.shutdownNow();
		Thread.yield();
		if(l.size()>0){
			System.out.println("The following articles failed: ");
			for(Runnable r: l){
				ArticleWorkUnit awu = (ArticleWorkUnit)r;
				System.out.println(awu.name);
			}
		}
		br.close();
		System.out.println("Done converting articles [" + articleStart + " - " + (articleStart+numArticlesProcessed)+")");
		System.out.println("Took "+(System.currentTimeMillis()-time)/1000.0+" to read "+(articleEnd-articleStart)+" articles.");
		System.out.println((System.currentTimeMillis()-time)/1000.0/(articleEnd-articleStart)+" per article.");
	}
	class ArticleWorkUnit implements Runnable{
		String article;
		String name;
		public ArticleWorkUnit(String s, String f){
			article=s;
			name=f;
		}
		public void run(){
			String noFormatting = removeWikiFormat(article);
			saveToFile(name, noFormatting);
		}
	}
		
	public static void main(String[] args) {
		final WikiProcessor w = new WikiProcessor();
		try {
			w.convertFile(new File("resources/unprocessed samples/enwiki-20080312-pages-articles.xml"), 50000, 100000);
			for(int i=1;i<52;i++)
				w.convertFile(new File("resources/unprocessed samples/enwiki-20080312-pages-articles.xml"), 100000*i, 100000*(i+1));
		} catch (IOException e) {
			e.printStackTrace();
		}
//		String text = w.getPage("http://en.wikipedia.org/wiki/Special:Export/Arabic_language");
//		w.saveToFile("test.txt", text);
//		text = w.removeWikiFormat(text);
//		w.saveToFile("test1.txt", text);
	}
}
