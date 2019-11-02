package com.adroit.ebooks.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;

/**
 * Servlet implementation class UploadServlet
 */
@WebServlet("/upload")
@MultipartConfig
public class UploadServlet extends HttpServlet {
	private static final String PART_NAME = "kpUpload";
	private static final String INVENTORY_FILE = "inventory.xml";
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = (Logger) LoggerFactory.getLogger(UploadServlet.class);

	/**
	 * Default constructor. 
	 */
	public UploadServlet() {
	}
	
	@Override
	public void init() throws ServletException {
		// get user home
		String userHomeDir = System.getProperty("user.home");
		String workingDir = userHomeDir.concat(File.separator).concat(".easyeb");
		File workingDirfile = new File(workingDir);
		LOG.debug("ebook200 : workingDir " + workingDir);
		if(workingDirfile.exists()) {
			LOG.debug("ebook200 : working dir exists");
		} else {
			boolean createdOrNot = workingDirfile.mkdir();
			LOG.debug("ebook200 : working dir does not exist. created" + createdOrNot);
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
		getServletContext().setAttribute("xmlDBFile", xmlDBFile);
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		LOG.debug("ebook100 ============Receiving file");

		// Retrieves <input type="file" name="file" multiple="true">
		LOG.debug("ebook100 ============Request parts : " + request.getParts().size());
		List<Part> fileParts = request.getParts().stream().filter(new Predicate<Part>() {
			@Override
			public boolean test(Part part) {
				LOG.debug("ebook100 ============part.getName()" + part.getName());
				return PART_NAME.equals(part.getName());
			}
		}).collect(Collectors.toList());
		LOG.debug("ebook100 ============fileParts : " + fileParts.size());

		if(!(fileParts.size() >= 1)) {
			response.getWriter().append("File upload failed");
		}

		for (Part filePart : fileParts) {
			String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString(); // MSIE fix.
			String tempDir = String.valueOf(request.getServletContext().getAttribute("javax.servlet.context.tempdir"));
			LOG.debug("ebook100 ============FileName of uploaded file : " + fileName);
			File uploadedFile = new File(tempDir.concat(File.separator).concat(fileName));

			InputStream fileIs = filePart.getInputStream();
			FileOutputStream fos = new FileOutputStream(uploadedFile);
			fileIs.transferTo(fos);
			LOG.debug("ebook100 Uploaded file path : " + uploadedFile.getAbsolutePath());

			// TODO Generate SKU
			// TODO Update XML
			
//			File shortenedPDF = PDFAPI.createSample(uploadedFile, fileName, tempDir);
//			LOG.debug("ebook1001 shortenedPDF file path : " + shortenedPDF.getAbsolutePath());

			// TODO Save both files to DB
			response.getWriter().append("File uploaded successfully : " + fileName);
		}
	}

}
