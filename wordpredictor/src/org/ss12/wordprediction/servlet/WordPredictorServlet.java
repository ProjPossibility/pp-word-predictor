/*
    This file is part of Word Predictor.

    Word Predictor is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Word Predictor is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Word Predictor.  If not, see <http://www.gnu.org/licenses/>. 
    
    This software was developed by members of Project:Possibility, a software 
    collaboration for the disabled.
    
    For more information, visit http://projectpossibility.org
*/

package org.ss12.wordprediction.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ss12.wordprediction.WordLoader;
import org.ss12.wordprediction.TreeMapWordPredictor;
import org.ss12.wordprediction.model.PredictionModel;

/**
 * Receives a request from the user and outputs the top 5 words.
 */
public class WordPredictorServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 887632007177487449L;
	private PredictionModel wp;

	/**
	 * This creates a new servlet.
	 */
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
		this.wp = new TreeMapWordPredictor(wl.getWords());
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
