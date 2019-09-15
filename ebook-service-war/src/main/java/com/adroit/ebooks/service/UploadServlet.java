package com.adroit.ebooks.service;

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
        System.out.println("============Receiving file");
		
		// Retrieves <input type="file" name="file" multiple="true">
        System.out.println("============Request parts : " + request.getParts().size());
		List<Part> fileParts = request.getParts().stream().filter(new Predicate<Part>() {
			@Override
			public boolean test(Part part) {
				System.out.println("============part.getName()" + part.getName());
				return PART_NAME.equals(part.getName());
			}
		}).collect(Collectors.toList());
		System.out.println("============fileParts : " + fileParts.size());
		
		if(!(fileParts.size() >= 1)) {
			response.getWriter().append("File upload failed");
		}

	    for (Part filePart : fileParts) {
	        String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString(); // MSIE fix.
	        System.out.println("============FileName : " + fileName);
	        response.getWriter().append("File uploaded successfully : " + fileName + "\n");
	        InputStream fileIs = filePart.getInputStream();
	        FileOutputStream fos = new FileOutputStream(fileName);
	        fileIs.transferTo(fos);
	        // TODO Log creation of file with details
	    }
	}

}
