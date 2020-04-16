/*******************************************************************************
 * Copyright (c) 2008 Tran Nam Quang.
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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;

import org.apache.poi.hwpf.extractor.WordExtractor;


public class MSWordParser extends MSOfficeParser {

	private static final String[] extensions = new String[] { "doc" }; //$NON-NLS-1$

	public String renderText(File file) throws ParseException {
		InputStream in = null;
		try {
			in = new FileInputStream(file);
			WordExtractor extractor = null;
			try {
				extractor = new WordExtractor(in);
			} catch (Exception e) {
				// This can happen if the file has the "doc" extension, but is
				// not a Word document
				throw new ParseException(file, "file corrupted");
			} finally {
				in.close();
			}
			return extractor.getText();
		} catch (FileNotFoundException e) {
			throw new ParseException(file, "file not found");
		} catch (IOException e) {
			throw new ParseException(file, "file not readable");
		}
	}

	public String[] getExtensions() {
		return extensions;
	}

	public String getFileType() {
		return "Microsoft Word";
	}

	public static void main(String[] args) throws ParseException, IOException {
		MSWordParser m = new MSWordParser();
		String text = m.renderText(new File("C:\\Users\\haha\\Desktop\\GriDoc课题离校保证书.doc"));
		System.out.println("MS Word识别结果字符数为：" + text.length());
		// FileWriter fw = new
		// FileWriter("C:\\Users\\haha\\Desktop\\GriDoc课题离校保证书.txt", false);
		// BufferedWriter bw = new BufferedWriter(fw);
		// bw.write(text);
		// bw.close();
		// fw.close();
		File file = new File("C:\\Users\\haha\\Desktop\\GriDoc1.txt");
		OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
		out.write(text.toCharArray());
		out.flush();
		out.close();
	}

}
