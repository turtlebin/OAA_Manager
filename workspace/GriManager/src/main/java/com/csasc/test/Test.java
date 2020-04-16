package com.csasc.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import gri.driver.manager.GriDocManager;
import gri.driver.model.Connection;
import gri.driver.model.process.Paragraph;

public class Test {
	public static void main(String[] args) {
		final Connection con = new Connection("", "EITP://localhost:9010", "aaa", "123");

		new Runnable() {
			@Override
			public void run() {
				// synchronized (con) {

				System.err.println("!!!!!!!!!!!!!!\n\n\n\n\n\n\n\n\n\n\n\n1111111111111111111");
				String filePath = "C:/Users/haha/Desktop/1";
				Paragraph paragraph = new Paragraph();
				paragraph.setId(16);
				GriDocManager manager = new GriDocManager(con);
				try {
					File file = new File(filePath);
					if (file.exists())
						file.delete();
					file.createNewFile();
					FileOutputStream outputStream = new FileOutputStream(file, true);
					int size;
					byte[] bytes = new byte[1000];
					while ((size = manager.readData(paragraph, bytes)) != -1) {
						outputStream.write(bytes, 0, size);
					}
					outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				System.err.println("ok1!!!!!!!!!!!!!!\n");
				System.err.println("ok1!!!!!!!!!!!!!!\n");
				System.err.println("ok1!!!!!!!!!!!!!!\n");
				System.err.println("ok1!!!!!!!!!!!!!!\n");
				System.err.println("ok1!!!!!!!!!!!!!!\n");
				System.err.println("ok1!!!!!!!!!!!!!!\n");
				System.err.println("ok1!!!!!!!!!!!!!!\n");
				System.err.println("ok1!!!!!!!!!!!!!!\n");
				System.err.println("ok1!!!!!!!!!!!!!!\n");

				System.err.println("ok1!!!!!!!!!!!!!!\n");
			}

			// }
		}.run();

		new Runnable() {
			@Override
			public void run() {
				// synchronized (con) {
				System.out.println("!!!!!!!!!!!!!!\n\n\n\n\n\n\n\n\n\n\n\n22222222222222");
				String filePath = "C:/Users/haha/Desktop/2";
				Paragraph paragraph = new Paragraph();
				paragraph.setId(18);
				GriDocManager manager = new GriDocManager(con);
				try {
					File file = new File(filePath);
					if (file.exists())
						file.delete();
					file.createNewFile();
					FileOutputStream outputStream = new FileOutputStream(file, true);
					int size;
					byte[] bytes = new byte[1000];
					while ((size = manager.readData(paragraph, bytes)) != -1) {
						outputStream.write(bytes, 0, size);
					}
					outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				System.err.println("ok2!!!!!!!!!!!!!!!!!!!!!!!!!\n");
				System.err.println("ok2!!!!!!!!!!!!!!!!!!!!!!!!!\n");

				System.err.println("ok2!!!!!!!!!!!!!!!!!!!!!!!!!\n");
				System.err.println("ok2!!!!!!!!!!!!!!!!!!!!!!!!!\n");

				System.err.println("ok2!!!!!!!!!!!!!!!!!!!!!!!!!\n");
				System.err.println("ok2!!!!!!!!!!!!!!!!!!!!!!!!!\n");
				System.err.println("ok2!!!!!!!!!!!!!!!!!!!!!!!!!\n");
				System.err.println("ok2!!!!!!!!!!!!!!!!!!!!!!!!!\n");
				System.err.println("ok2!!!!!!!!!!!!!!!!!!!!!!!!!\n");
				System.err.println("ok2!!!!!!!!!!!!!!!!!!!!!!!!!\n");

			}
			// }
		}.run();

	}
}
