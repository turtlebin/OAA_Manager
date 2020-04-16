/*******************************************************************************
 * Copyright (c) 2007, 2008 Tran Nam Quang.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Tran Nam Quang - initial API and implementation
 *******************************************************************************/

package gri.engine.parse;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.pdmodel.encryption.StandardDecryptionMaterial;
import org.apache.pdfbox.util.PDFTextStripper;

/**
 */
public class PDFParser extends Parser {

	private static String[] extensions = new String[] { "pdf" }; //$NON-NLS-1$

	public String renderText(File file) throws ParseException {
		PDDocument pdfDoc = null;
		try {
			pdfDoc = PDDocument.load(file);
			if (pdfDoc.isEncrypted()) {
				try {
					pdfDoc.openProtection(new StandardDecryptionMaterial(""));
				} catch (Exception e) {
					throw new ParseException(file, "no_extraction_permission");
				}
			}
			PDFTextStripper stripper = new PDFTextStripper();
			StringWriter writer = new StringWriter();
			stripper.writeText(pdfDoc, writer);
			return writer.toString();
		} catch (IOException e) {
			throw new ParseException(file, "file_not_readable");
		} finally {
			if (pdfDoc != null) {
				try {
					pdfDoc.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public Document parse(File file) throws ParseException {
		PDDocument pdfDoc = null;
		try {
			// Check if PDF file is encrypted
			pdfDoc = PDDocument.load(file);
			if (pdfDoc.isEncrypted()) {
				try {
					pdfDoc.openProtection(new StandardDecryptionMaterial(""));
				} catch (Exception e) {
					throw new ParseException(file, "no_extraction_permission");
				}
			}

			// Get tags and contents
			PDFTextStripper stripper = new PDFTextStripper();
			StringWriter writer = new StringWriter();
			stripper.writeText(pdfDoc, writer);
		//	DocFetcher.getInstance().setExceptionHandlerEnabled(true);
			PDDocumentInformation pdInfo = pdfDoc.getDocumentInformation();
			String[] metaData = new String[] { pdInfo.getTitle(), pdInfo.getAuthor(), pdInfo.getSubject(),
					pdInfo.getKeywords(), };
			for (String field : metaData)
				if (field != null)
					writer.append(" ").append(field); //$NON-NLS-1$
			return new Document(file, metaData[0], writer.getBuffer()).addAuthor(metaData[1]);
		} catch (IOException e) {
			throw new ParseException(file, "file_not_readable");
		} finally {
			if (pdfDoc != null) {
				try {
					pdfDoc.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public String getFileType() {
		return "PDF";
	}

	public String[] getExtensions() {
		return extensions;
	}
	
	public static void main(String[] args) throws ParseException {
		PDFParser p = new PDFParser();
		String text = p.renderText(new File("C:\\Users\\haha\\Desktop\\GriDoc课题离校保证书.pdf"));
		System.out.println("PDF识别结果字符数为：" + text.length());
	}

}
