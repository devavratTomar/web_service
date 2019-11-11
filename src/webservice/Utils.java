package webservice;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class Utils {
	static public byte[] readFileData(File file) throws IOException {
		FileInputStream fileIn = null;
		
		int fileSize = (int) file.length();
		byte[] fileData = new byte[fileSize];
		try {
			fileIn = new FileInputStream(file);
			fileIn.read(fileData);
		} finally {
			if (fileIn != null)
				fileIn.close();
		}
		
		return fileData;
	}
}
