/**
 * 
 */
package org.ss12.wordprediction.servlet;

import org.mortbay.jetty.Server;
import org.mortbay.jetty.servlet.Context;
import org.mortbay.jetty.servlet.ServletHolder;

/**
 * Creates a web server and adds a {@link WordPredictorServlet}.
 */
public class WebServer {
	public static void main(String[] args) throws Exception {
		Server server = new Server(8080);
		Context root = new Context(server, "/", Context.SESSIONS);
		root.addServlet(new ServletHolder(new WordPredictorServlet()), "/predict");
		server.start();
	}
}
