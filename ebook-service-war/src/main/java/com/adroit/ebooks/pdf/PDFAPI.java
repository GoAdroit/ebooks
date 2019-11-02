package com.adroit.ebooks.pdf;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfSmartCopy;

/**
 * All PDF related APIs 
 * @author praku
 *
 */
public class PDFAPI {
	private static final String SAMPLEFILENAME = "-sample";

	public static File createSample(File originalPDF, String fileName, String dir) {
		File sampleFile = new File(dir.concat(File.separator).concat(fileName).concat(SAMPLEFILENAME).concat(".pdf"));

		try {
			Document document;
			PdfReader reader = new PdfReader(Files.readAllBytes(originalPDF.toPath()));
			int numOfPagesInOriginalPDF = reader.getNumberOfPages();
			if(numOfPagesInOriginalPDF > 100) {

				document = new Document();
				PdfSmartCopy pdfCopy = new PdfSmartCopy(document, new BufferedOutputStream(new FileOutputStream(sampleFile)));
				document.open();

				// add first 10 pages for sure
				for (int p = 1; p < 12; p++) {
					PdfImportedPage page = pdfCopy.getImportedPage(reader, p);
					pdfCopy.addPage(page);
				}
				
				document.close();
				
				return sampleFile;
			} else {
				System.out.println("WARN ebook1001 ============ Couldn't create sample file");
			}
		} catch (IOException | DocumentException e) {
			e.printStackTrace();
		}
		return sampleFile;
	}
}
