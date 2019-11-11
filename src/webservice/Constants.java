package webservice;

import java.io.File;

public class Constants {
	// contains all the constants required.
	
	static final File WEB_ROOT = new File(".");
	static final String DEFAULT_FILE = "index.html";
	static final String FILE_NOT_FOUND = "404.html";
	static final String METHOD_NOT_SUPPORTED = "not_supported.html";
	
	static final String GET = "GET";
	static final String NOT_IMPLEMENTED = "HTTP/1.1 501 Not Implemented";
	static final String OK_RESPONSE = "HTTP/1.1 200 OK";
}
