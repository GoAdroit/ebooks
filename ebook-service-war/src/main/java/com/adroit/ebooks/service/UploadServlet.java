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

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.LoggerFactory;

import com.adroit.ebooks.pdf.PDFAPI;

import ch.qos.logback.classic.Logger;

/**
 * Servlet implementation class UploadServlet
 */
@WebServlet("/upload")
@MultipartConfig
public class UploadServlet extends HttpServlet {
	private static final String PART_NAME = "kpUpload";
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = (Logger) LoggerFactory.getLogger(UploadServlet.class);

	/**
	 * Default constructor. 
	 */
	public UploadServlet() {
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
		try {
			LOG.debug("Receiving file");

			// Retrieves <input type="file" name="file" multiple="true">
			LOG.debug("Request parts : " + request.getParts().size());
			List<Part> fileParts = request.getParts().stream().filter(new Predicate<Part>() {
				@Override
				public boolean test(Part part) {
					LOG.debug("part.getName()" + part.getName());
					return PART_NAME.equals(part.getName());
				}
			}).collect(Collectors.toList());
			LOG.debug("fileParts : " + fileParts.size());

			if(!(fileParts.size() >= 1)) {
				response.getWriter().append("File upload failed");
			}

			for (Part filePart : fileParts) {
				String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
				File uploadDir = new File(String.valueOf(getServletContext().getAttribute("xmlDBFile"))).getParentFile();
				LOG.debug("uploadDir : " + uploadDir);
				LOG.debug("FileName of uploaded file : " + fileName);
				File uploadedFile = new File(uploadDir.getAbsolutePath().concat(File.separator).concat(fileName));

				InputStream fileIs = filePart.getInputStream();
				FileOutputStream fos = new FileOutputStream(uploadedFile);
				fileIs.transferTo(fos);
				LOG.debug("Uploaded file path : " + uploadedFile.getAbsolutePath());

				// TODO Generate SKU
				// TODO Update XML

				// shorten file
				PDFAPI.createSample(uploadedFile);

				// TODO Save both files to DB
				LOG.debug("File uploaded successfully");
				response.getWriter().append("File uploaded successfully : " + fileName);
			}
		} catch (Exception e) {
			response.getWriter().append("Error occurred during file upload : " + e.getMessage());
			LOG.debug("Exception occurred : \n" + ExceptionUtils.getStackTrace(e));
		}
	}

}
