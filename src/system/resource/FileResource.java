package system.resource;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class FileResource implements AbstractResource {

	private FileResource () {
		fName = null;
		file = null;
		reader = null;
	}
	
	String fName;
	File file;
	FileReader reader;
	
	public FileResource(String fName) {
		this();
		this.fName = fName;
		this.file = new File(fName);
	}

	@Override
	public InputStreamReader getReader() {
		if (isAvailable()) {
			try {
				return (reader != null) ? reader : (reader = (new FileReader(file)));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	public boolean isAvailable() {
		return file != null && file.exists() && file.canRead();
	}

	@Override
	public String getDescription() {
		return "File-Resource: \"" + fName + "\", available: " + isAvailable();
	}

	@Override
	public void close() {
		if (reader != null) {
			try {
				reader.close();
			} catch (IOException e) {e.printStackTrace();}
		}
		//no need to nullify file!!!
	}

}
