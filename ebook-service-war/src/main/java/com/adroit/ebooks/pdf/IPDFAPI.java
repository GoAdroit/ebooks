package com.adroit.ebooks.pdf;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface IPDFAPI {
	public File createSample(File originalPDF);
	
	/**
	 * 
	 * @param originalPDF
	 * @return list of.png File instances; each file is the image representation of a page in the source PDF
	 */
	public List<File> getPagesAsImages(File originalPDF);
	
	/**
	 * Add images to new PDF file
	 * 
	 * @param filesToMerge
	 * @param destinationFile
	 * @return new PDF containing all images passed in
	 * 
	 * @throws  throws IOException
	 */
	public File addImagesToNewPDF(List<File> filesToMerge, String destinationFile)  throws IOException;
}
