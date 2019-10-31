package com.adroit.ebooks.search.test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.adroit.ebooks.search.ISearchBook;
import com.adroit.ebooks.search.SearchBookImpl;

class SearchBookTest {
	
	private String searchType;
	private String tempDirPath;

	@BeforeEach
	void setUp() throws Exception {
		searchType = "drive";
		tempDirPath = "/Users/praku/programs/tomcat-9.0.10/apache-tomcat-9.0.10/work";
	}

	@Test
	void testSearchResults() {
		ISearchBook bookSearch = new SearchBookImpl(searchType, tempDirPath);
		String[] bookSearchResult = bookSearch.searchResults("fda");
		System.out.println(bookSearchResult.length);
		for(String fileName : bookSearchResult) {
			System.out.println(fileName);
		}
		assertNotNull(bookSearchResult);
	}

}
