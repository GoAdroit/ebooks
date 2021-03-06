package com.adroit.ebooks.service;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;

/**
 * Servlet that responds with a search page after user's successful login 
 */
@WebServlet("/user")
public class UserHomeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = (Logger) LoggerFactory.getLogger(UserHomeServlet.class);
       
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		File xmlDBFile = (File) getServletContext().getAttribute("xmlDBFile");
		LOG.debug("userservlet xmlDBFile : " + xmlDBFile.getAbsolutePath());		
		response.sendRedirect("search.html");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
