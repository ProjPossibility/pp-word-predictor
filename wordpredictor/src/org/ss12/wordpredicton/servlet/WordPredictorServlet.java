package org.ss12.wordpredicton.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ss12.wordprediction.WordLoader;
import org.ss12.wordprediction.WordPredictor;
import org.ss12.wordprediction.model.PredictionModel;

public class WordPredictorServlet extends HttpServlet {

	private PredictionModel wp;

	public WordPredictorServlet() {
		WordLoader wl = new WordLoader(1);
		try {
			wl.loadDictionary(new File(
					"resources/dictionaries/converted/plain.dat"));
			wl.loadFrequenciess(new File(
					"resources/dictionaries/converted/freq.dat"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.wp = new WordPredictor(wl.getWords());
	}

	public void doGet(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		PrintWriter out = res.getWriter();
		
		String ppw = req.getParameter("prePreWord");
		String pw = req.getParameter("preWord");
		String cw = req.getParameter("currentWord");

		boolean canCorrect = true;
		/*
		 * if(ppw == null || ppw.equals("")) { ppw = ""; canCorrect = false; }
		 * if(pw == null || pw.equals("")) { pw = ""; canCorrect = false; }
		 */
		if (cw == null || cw.equals("")) {
			cw = "";
			canCorrect = false;
		}
		out.print("<html>\n" + "<head>\n" + "<title>Word Prediction</title>\n"
				+ "</head>\n" + "<body>\n" + "<h1>Word Prediction</h1>\n");

		if (canCorrect) {
			out.print("<h3>Top 5 Suggestions</h3>");

			String[] suggestions = wp
					.getSuggestionsFromDic(cw.toLowerCase(), 5);

			out.print("<ol>");

			for (String suggestion : suggestions) {
				out.print("<li>" + suggestion + "</li>");
			}

			out.print("</ol>");
		}

		out
				.print("<form name=\"input\" action=\"http://localhost:8080/predict\" method=\"get\">\n"
						+ "Type in the pre pre word:\n"
						+ "<input type=\"text\" name=\"prePreWord\" value=\""
						+ ppw
						+ "\">\n"
						+ "<br>\n"
						+ "<br>\n"
						+ "Type in the pre word:\n"
						+ "<input type=\"text\" name=\"preWord\" value=\""
						+ pw
						+ "\">\n"
						+ "<br>\n"
						+ "<br>\n"
						+ "Type in part of the word:\n"
						+ "<input type=\"text\" name=\"currentWord\" value=\""
						+ cw
						+ "\">\n"
						+ "<br>\n"
						+ "<br>\n"
						+ "<input type=\"submit\" value=\"Get Suggestions\">\n"
						+ "</form>" + "</body>" + "</html>");
		out.close();
	}
}
