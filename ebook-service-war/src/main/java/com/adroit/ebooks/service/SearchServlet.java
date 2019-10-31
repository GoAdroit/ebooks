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
		response.getWriter().append("What to search: ").append(request.getParameter("bname"));
		String bookName = request.getParameter("bname");
		System.out.println("bname param : " + bookName);
		String tempDir = String.valueOf(request.getServletContext().getAttribute("javax.servlet.context.tempdir"));
		ISearchBook bookSearch = new SearchBookImpl("drive", tempDir);
		String[] bookNames = bookSearch.searchResults(bookName);
		
		StringBuffer responseString = new StringBuffer();
		responseString.append("\nFound ").append(" results :");
		for(String book : bookNames) {
			responseString.append("\n").append(book);
		}
		
		response.getWriter().append(responseString.toString());
		
	}

}
