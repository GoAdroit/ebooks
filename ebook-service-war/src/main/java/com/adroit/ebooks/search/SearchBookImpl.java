package com.adroit.ebooks.search;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;

public class SearchBookImpl implements ISearchBook {
	
	private String searchType;
	private String tempDirPath;
	private static final Logger LOG = (Logger) LoggerFactory.getLogger(SearchBookImpl.class);
	// TODO use builder to set the type of search : 1. Tomcat drive 2. XML file 3. SQL for DB
	public SearchBookImpl(String searchType, String tempDir) {
		this.searchType = searchType;
		this.tempDirPath = tempDir;
	}

	@Override
	public String[] searchResults(String serachKey) {
		if(searchType.equals("drive")) {
			File tempDir = new File(tempDirPath);
			if(tempDir.isDirectory()) {
				Iterator<File> fileIterator = FileUtils.iterateFiles(tempDir, null, true);
				List<String> fileNames = new ArrayList<String>();
				while(fileIterator.hasNext()) {
					File currentFile = fileIterator.next();
					if(currentFile.getName().contains(serachKey)) {
						LOG.debug("currentFile : " + currentFile.getName());
						fileNames.add(FilenameUtils.getBaseName(currentFile.getName()));
					}
				}
				return fileNames.toArray(new String[fileNames.size()]);
			}
		}
		return null;
	}
}
