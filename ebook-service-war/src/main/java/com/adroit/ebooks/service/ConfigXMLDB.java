package com.adroit.ebooks.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;

@WebListener
public class ConfigXMLDB implements ServletContextListener {
	private static final String INVENTORY_FILE = "inventory.xml";
	private static final Logger LOG = (Logger) LoggerFactory.getLogger(ConfigXMLDB.class);

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		// get user home
		String userHomeDir = System.getProperty("user.home");
		String workingDir = userHomeDir.concat(File.separator).concat(".easyeb");
		File workingDirfile = new File(workingDir);
		LOG.debug("ebook200 : workingDir " + workingDir);
		if(workingDirfile.exists()) {
			LOG.debug("ebook200 : working dir exists");
		} else {
			boolean createdOrNot = workingDirfile.mkdir();
			LOG.debug("ebook200 : working dir does not exist. created " + createdOrNot);
		}

		// extract xml from war
		InputStream dbFileIS = this.getClass().getClassLoader().getResourceAsStream(INVENTORY_FILE);
		FileOutputStream fos;
		File xmlDBFile = new File(workingDir.concat(File.separator).concat(INVENTORY_FILE));
		// check if xml exists
		if(!xmlDBFile.exists()) {
			// if it doesn't, copy it to user-home
			LOG.debug("ebook200 : xmlDBFile does not exist. creating now");
			try {
				fos = new FileOutputStream(xmlDBFile);
				dbFileIS.transferTo(fos);
				LOG.debug("ebook200 : xmlDBFile created");
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			LOG.debug("ebook200 : xmlDBFile exists already");
		}
		// TODO make it available to other servlets
		sce.getServletContext().setAttribute("xmlDBFile", xmlDBFile);
	}
}
