package com.adroit.ebooks.pdf;

import java.io.File;

import org.apache.commons.io.FilenameUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PDFAPITest {

	@BeforeEach
	void setUp() throws Exception {
	}

	@Test
	static void testCreateSample() {
		File originalPDFFile = new File("/Users/praku/.easyeb/d2-config.pdf");
		System.out.println("Base name : " + FilenameUtils.getBaseName(originalPDFFile.getAbsolutePath()));
		System.out.println("File name from utils : " + FilenameUtils.getName(originalPDFFile.getAbsolutePath()));
		System.out.println("File name  : " + originalPDFFile.getName());
		System.out.println("Base name from File name  : " + FilenameUtils.getBaseName(originalPDFFile.getName()));
		
		PdfApiItext pdfApiItext = new PdfApiItext();
//		File sampleFile = pdfApiItext.createSample(originalPDFFile);
		
		PdfApiApache pdfApiApache = new PdfApiApache();
		File sampleFile = pdfApiItext.createSample(originalPDFFile);
		
		System.out.println(sampleFile.getAbsolutePath());
	}
	
	public static void main (String[] args) {
		testCreateSample();
	}
}
