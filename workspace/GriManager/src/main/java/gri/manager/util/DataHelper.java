package gri.manager.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import gri.driver.manager.GriDocManager;
import gri.driver.model.process.Paragraph;

public class DataHelper {

	public boolean saveData(String filePath, GriDocManager manager, Paragraph paragraph) {
		manager = manager.copy();
		try {
			File file = new File(filePath);
			if (file.exists())
				file.delete();
			file.createNewFile();
			FileOutputStream outputStream = new FileOutputStream(file, true);
			int size;
			byte[] bytes = new byte[10000];
			while ((size = manager.readData(paragraph, bytes)) != -1) {
				outputStream.write(bytes, 0, size);
			}
			outputStream.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean uploadData(String filePath, GriDocManager manager, Paragraph paragraph) {
		manager = manager.copy();
		try {
			FileInputStream inputStream = new FileInputStream(new File(filePath));
			int size = inputStream.available();
			if (size <= 10000) {
				byte[] bytes = new byte[size];
				inputStream.read(bytes);
				inputStream.close();
				return manager.writeFile(paragraph, bytes, false);
			} else {
				boolean succeed = true;
				byte[] bytes = new byte[10000];
				int ret = inputStream.read(bytes);
				if (!manager.writeFile(paragraph, bytes, false))
					succeed = false;
				while ((ret = inputStream.read(bytes)) != -1) {
					byte[] b2 = new byte[ret];
					System.arraycopy(bytes, 0, b2, 0, ret);
					if (!manager.writeFile(paragraph, b2, true)) {
						succeed = false;
						break;
					}
				}
				inputStream.close();
				return succeed;
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

}
