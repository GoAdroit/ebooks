package com.adroit.ebooks.service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.adroit.ebooks.search.ISearchBook;
import com.adroit.ebooks.search.SearchBookImpl;

/**
 * Searches and returns list of books matching search criteria
 * @author praku
 *
 */
@WebServlet("/search")
public class SearchServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		String bookName = request.getParameter("bname");
		String tempDir = String.valueOf(request.getServletContext().getAttribute("javax.servlet.context.tempdir"));
		ISearchBook bookSearch = new SearchBookImpl("drive", tempDir);
		String[] bookNames = bookSearch.searchResults(bookName);
		
		StringBuffer responseString = new StringBuffer();
		responseString.append("<br><h1>EBooks matching ").append(bookName).append("</h1><br>");
		responseString.append("<table>");
		responseString.append("<tr>");
		responseString.append("<th>Title</th>");
		responseString.append("<th>Sample</th>");
		responseString.append("<th>Buy</th>");
		for(String book : bookNames) {
			responseString.append("<tr>");
			responseString.append("<td>").append(book).append("</td>");
			responseString.append("<td>").append("Sample").append("</td>");
			responseString.append("<td>").append("Buy").append("</td>");
			responseString.append("</tr>");
		}
		responseString.append("</table>");
		response.getWriter().append(responseString.toString());
	}
}