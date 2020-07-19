package com.adroit.ebooks.pdf;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.LoggerFactory;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfEncryptor;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfSmartCopy;
import com.lowagie.text.pdf.PdfStamper;
import com.lowagie.text.pdf.PdfWriter;

import ch.qos.logback.classic.Logger;

/**
 * All PDF related APIs 
 * @author praku
 *
 */
public class PdfApiItext implements IPDFAPI {
	private static final String SAMPLEFILENAME = "-sample";
	private static final Logger LOG = (Logger) LoggerFactory.getLogger(PdfApiItext.class);

	public File createSample(File originalPDF) {
		String fileName = FilenameUtils.getBaseName(originalPDF.getName());
		LOG.debug("fileName : " + fileName);
		File sampleFile = new File(originalPDF.getParent().concat(File.separator).concat(fileName).concat(SAMPLEFILENAME).concat(".pdf"));
		LOG.debug("sampleFile to be created : " + sampleFile);

		try {
			Document document;
			PdfReader reader = new PdfReader(Files.readAllBytes(originalPDF.toPath()));
			int numOfPagesInOriginalPDF = reader.getNumberOfPages();
			int maxPagesInSample = 1;

			if(numOfPagesInOriginalPDF > 1) {
				int noOfPagesInSample = numOfPagesInOriginalPDF * 10 / 100;
				maxPagesInSample = noOfPagesInSample <= 0 ? maxPagesInSample : noOfPagesInSample; 
			}

			LOG.debug("numOfPagesInOriginalPDF : " + numOfPagesInOriginalPDF);
			LOG.debug("maxPagesInSample : " + maxPagesInSample);

			document = new Document();
			PdfSmartCopy pdfCopy = new PdfSmartCopy(document, new BufferedOutputStream(new FileOutputStream(sampleFile)));
//			pdfCopy.setEncryption("password".getBytes(), "".getBytes(), PdfWriter.ALLOW_SCREENREADERS, PdfWriter.STANDARD_ENCRYPTION_128); // with password and copy, print restrictions
			pdfCopy.setEncryption("".getBytes(), "".getBytes(), PdfWriter.ALLOW_SCREENREADERS, PdfWriter.STANDARD_ENCRYPTION_128); // without password and copy, print restrictions
			document.open();
//			PdfEncryptor.encrypt(reader, new FileOutputStream(sampleFile), "passwd".getBytes(), "".getBytes(), PdfWriter.STANDARD_ENCRYPTION_128, false);

			for (int p = 1; p <= maxPagesInSample; p++) {
				PdfImportedPage page = pdfCopy.getImportedPage(reader, p);
				pdfCopy.addPage(page);
			}

			document.close();

			LOG.debug("shortenedPDF file path : " + sampleFile.getAbsolutePath());
			return sampleFile;
		} catch (IOException e) {
			LOG.debug("IOException occurred during sample creation: \n" + ExceptionUtils.getStackTrace(e));
		} catch (DocumentException e) {
			LOG.debug("DocumentException occurred during sample creation: \n" + ExceptionUtils.getStackTrace(e));
		}
		return sampleFile;
	}

	@Override
	public List<File> getPagesAsImages(File originalPDF) {
		return null;
	}

	@Override
	public File addImagesToNewPDF(List<File> filesToMerge, String destinationFile) throws IOException {
		return null;
	}
}
