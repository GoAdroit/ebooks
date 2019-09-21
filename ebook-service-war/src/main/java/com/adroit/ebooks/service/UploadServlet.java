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

import com.adroit.ebooks.pdf.PDFAPI;

/**
 * Servlet implementation class UploadServlet
 */
@WebServlet("/upload")
@MultipartConfig
public class UploadServlet extends HttpServlet {
	private static final String PART_NAME = "kpUpload";
	private static final long serialVersionUID = 1L;

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
		System.out.println("ebook100 ============Receiving file");

		// Retrieves <input type="file" name="file" multiple="true">
		System.out.println("ebook100 ============Request parts : " + request.getParts().size());
		List<Part> fileParts = request.getParts().stream().filter(new Predicate<Part>() {
			@Override
			public boolean test(Part part) {
				System.out.println("ebook100 ============part.getName()" + part.getName());
				return PART_NAME.equals(part.getName());
			}
		}).collect(Collectors.toList());
		System.out.println("ebook100 ============fileParts : " + fileParts.size());

		if(!(fileParts.size() >= 1)) {
			response.getWriter().append("File upload failed");
		}

		for (Part filePart : fileParts) {
			String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString(); // MSIE fix.
			String tempDir = String.valueOf(request.getServletContext().getAttribute("javax.servlet.context.tempdir"));
			System.out.println("ebook100 ============FileName of uploaded file : " + fileName);
			File uploadedFile = new File(tempDir.concat(File.separator).concat(fileName));

			InputStream fileIs = filePart.getInputStream();
			FileOutputStream fos = new FileOutputStream(uploadedFile);
			fileIs.transferTo(fos);
			System.out.println("ebook100 Uploaded file path : " + uploadedFile.getAbsolutePath());

			// TODO Use EHCache

			// TODO Create a shortened version of the PDF
			File shortenedPDF = PDFAPI.createSample(uploadedFile, fileName, tempDir);
			System.out.println("ebook100 shortenedPDF file path : " + shortenedPDF.getAbsolutePath());

			// TODO Save both files to DB

			response.getWriter().append("File uploaded successfully : " + fileName + "\n");
		}
	}

}
