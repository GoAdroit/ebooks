package com.adroit.ebooks.pdf;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.io.FilenameUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageContentStream.AppendMode;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.StandardProtectionPolicy;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;

public class PdfApiApache implements IPDFAPI {
	public static final int KEY_40 = 40;
	public static final int KEY_128 = 128;
	public static final int KEY_256 = 256;
	private static final Logger LOG = (Logger) LoggerFactory.getLogger(PdfApiApache.class);

	@Override
	public File createSample(File originalPDF) {
		File sampleFile = null;
		try {
			sampleFile = File.createTempFile("new", ".pdf");
			PDDocument doc = PDDocument.load(originalPDF);

			// Can use any one of the above defined encryption key either 40,128,256
			// But 256 will be available in PDFBox 2.0 or above versions.
			int key = KEY_128;

			// Giving permission to the user
			AccessPermission permissions = new AccessPermission();

			// Disable printing, everything else is allowed
			permissions.setCanPrint(false);
			permissions.setCanExtractContent(false);
			permissions.setCanModify(false);
			permissions.setCanAssembleDocument(false);
			permissions.setCanPrintDegraded(false);
			// setting for readonly access
			permissions.setReadOnly();

			// Owner password (to open the file with all permissions) is "owner@123"
			// User password (to open the file but with restricted permissions) is user@123

//			StandardProtectionPolicy spp = new StandardProtectionPolicy("owner@123", "user", permissions);
			StandardProtectionPolicy spp = new StandardProtectionPolicy("owner", "user", permissions);
			spp.setEncryptionKeyLength(key);
			spp.setPermissions(permissions);
			doc.protect(spp);

			doc.save(sampleFile);
			doc.close();
		} catch (IOException e) {}

		return sampleFile;
	}

	@Override
	public List<File> getPagesAsImages(File originalPDF) {
		String destDir = originalPDF.getParent();
		String destDirName = FilenameUtils.getBaseName(originalPDF.getName()).concat("_Page_Images_").concat(String.valueOf(new Date().toInstant().getEpochSecond()));
		destDir = destDir.concat(File.separator).concat(destDirName);
		File destinationDir = new File(destDir);
		List<File> returnFilesList = new ArrayList<File>();
		try {
			if (originalPDF.exists()) {
				LOG.debug("Images copied to Folder Location: " + destinationDir.getAbsolutePath());
				PDDocument document = PDDocument.load(originalPDF);
				PDFRenderer pdfRenderer = new PDFRenderer(document);

				int numberOfPages = document.getNumberOfPages();
				LOG.debug("Total number of pages : " + numberOfPages);

				String fileName = originalPDF.getName().replace(".pdf", "");
				String fileExtension = "png";
				/*
				 * 600 dpi give good image clarity but size of each image is 2x times of 300 dpi.
				 * For ex:
				 * 1. For 300dpi 04-Request-Headers_2.png expected size is 797 KB
				 * 2. For 600dpi 04-Request-Headers_2.png expected size is 2.42 MB
				 */
				int dpi = 300;// use less dpi to save more space in harddisk. For professional usage you
								// can use more than 300dpi

				for (int i = 0; i < numberOfPages; ++i) {
					File outPutFile = new File(destDir + File.separator + fileName + "_" + (i + 1) + "." + fileExtension);
					outPutFile.getParentFile().mkdirs();
					
					BufferedImage bImage = pdfRenderer.renderImageWithDPI(i, dpi, ImageType.RGB);
					ImageIO.write(bImage, fileExtension, outPutFile);
					returnFilesList.add(outPutFile);
				}

				document.close();
				LOG.debug("Converted Images are saved at -> " + destinationDir.getAbsolutePath());
			} else {
				LOG.error(originalPDF.getName() + " Source PDF file does not exist");
			}
		} catch (IOException e) {

		}
		return returnFilesList;
	}

	@Override
	public File addImagesToNewPDF(List<File> filesToMerge, String destinationFile) throws IOException {
		File destFile = new File(destinationFile);
		if(destFile.exists()) {
			destFile.delete();
		}
		PDDocument doc = new PDDocument();

		int i = 1;
		for(File imageFile : filesToMerge) {
			System.out.println("Adding images : \n");
			System.out.println(imageFile.getAbsolutePath());
			PDPage nextPage = new PDPage(PDRectangle.A4);

			System.out.println("Created new blank page");
			
			PDImageXObject pdImage = PDImageXObject.createFromFile(imageFile.getAbsolutePath(), doc);
			PDPageContentStream contentStream = new PDPageContentStream(doc, nextPage);
			contentStream.drawImage(pdImage, 0, 0, pdImage.getWidth() / 4.25f, pdImage.getHeight() / 4.25f);
			
			System.out.println("Drawing image complete for " + i++);
			doc.addPage(nextPage);
			contentStream.close();
		}
		
		doc.save(destFile);
		doc.close();

		System.out.println("Returning final file ");
		return null;
	}

	public static void main(String[] args) {
		IPDFAPI pdfApiApache = new PdfApiApache();

		// sample creation
//		File sampleFile = pdfApiApache.createSample(originalPDFFile);
//		System.out.println(sampleFile.getAbsolutePath());

		// pages to images
//		File originalPDFFile = new File("/Users/praku/.easyeb/d2-config-short.pdf");
//		String mergedFilePath = "/Users/praku/.easyeb/imagified_425_d2-config-short.pdf";

//		File originalPDFFile = new File("/Users/praku/.easyeb/PRASANNA_medical_reports.pdf");
//		String mergedFilePath = "/Users/praku/.easyeb/imagified_5_PRASANNA_medical_reports.pdf";

//		File originalPDFFile = new File("/Users/praku/.easyeb/Rice_cooker_order.pdf");
//		String mergedFilePath = "/Users/praku/.easyeb/imagified_425_Rice_cooker_order.pdf";
		

		File originalPDFFile = new File("/Users/praku/.easyeb/Physics-I.pdf");
		String mergedFilePath = "/Users/praku/.easyeb/imagified_425_Physics-I.pdf";

		List<File> imageFilesFromPDF = pdfApiApache.getPagesAsImages(originalPDFFile);

		
		System.out.println("Starting to create merged file : " + mergedFilePath);
		
		try {
			pdfApiApache.addImagesToNewPDF(imageFilesFromPDF, mergedFilePath);

			System.out.println("Merge complete : " + mergedFilePath);
		} catch (IOException e) {
			System.out.println("Error during merge : " + e.getMessage());
			e.printStackTrace();
		}
	}
	
}
