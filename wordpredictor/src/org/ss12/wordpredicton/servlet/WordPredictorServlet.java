package org.ss12.wordpredicton.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class WordPredictorServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		PrintWriter out = res.getWriter();

		String ppw = req.getParameter("prePreWord");
		String pw = req.getParameter("preWord");
		String cw = req.getParameter("currentWord");

		boolean canCorrect = true;
		
		if(ppw == null || cw.equals("")) {
			ppw = "";
			canCorrect = false;
		}
		if(pw == null || cw.equals("")) {
			pw = "";
			canCorrect = false;
		}
		if(cw == null || cw.equals("")) {
			cw = "";
			canCorrect = false;
		}
		out
				.print("<html>\n"
						+ "<head>\n"
						+ "<title>Word Prediction</title>\n"
						+ "</head>\n"
						+ "<body>\n"
						+ "<h1>Word Prediction</h1>\n");
		
		if(canCorrect) {
			out.print("hello");
		}
		
		out
				.print("<form name=\"input\" action=\"http://localhost:8080/predict\" method=\"get\">\n"
						+ "Type in the pre pre word:\n"
						+ "<input type=\"text\" name=\"prePreWord\" value=\"" + ppw + "\">\n"
						+ "<br>\n"
						+ "<br>\n"
						+ "Type in the pre word:\n"
						+ "<input type=\"text\" name=\"preWord\" value=\"" + pw + "\">\n"
						+ "<br>\n"
						+ "<br>\n"
						+ "Type in part of the word:\n"
						+ "<input type=\"text\" name=\"currentWord\" value=\"" + cw + "\">\n"
						+ "<br>\n"
						+ "<br>\n"
						+ "<input type=\"submit\" value=\"Get Suggestions\">\n"
						+ "</form>"
						+ "</body>"
						+ "</html>");
		out.close();
	}
}
