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

/**
 * 
 */
package org.ss12.wordprediction.servlet;

import org.mortbay.jetty.Server;
import org.mortbay.jetty.servlet.Context;
import org.mortbay.jetty.servlet.ServletHolder;

/**
 * Creates a web server and adds a {@link WordPredictorServlet}.
 * Connect at http://localhost:8080/predict
 */
public class WebServer {
	public static void main(String[] args) throws Exception {
		Server server = new Server(8080);
		Context root = new Context(server, "/", Context.SESSIONS);
		// root.addServlet(new ServletHolder(new WordPredictorServlet()), "/predict");
		root.addServlet(new ServletHolder(new WordPredictorService()), "/predictservice");
		server.start();
	}
}
