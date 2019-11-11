package webservice;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import java.util.Date;
import java.util.StringTokenizer;

import webservice.Constants;
import webservice.Utils;


public class ClientHandler implements Runnable {
	/*
	 * This class would handle the client http request.
	 * Read the request from the client.
	 * Send response to the client.
	*/
	
	private Socket connector;
	
	public ClientHandler(Socket c) {
		// pass the socket to the constructor for connecting to the web server
		connector = c;
	}
	
	@Override
	public void run() {
		// TODO: Should these be class members?
		
		/*
		 * Code for testing the worker pool. 
		 */

		/*
        for (int i = 0; i<=5; i++) 
        { 
            if (i==0) 
            { 
                Date d = new Date(); 
                SimpleDateFormat ft = new SimpleDateFormat("hh:mm:ss"); 
                System.out.println("Initialization Time for"
                        + " task name - "+ connector.toString() +" = " +ft.format(d));    
                //prints the initialization time for every task  
            } 
            else
            { 
                Date d = new Date(); 
                SimpleDateFormat ft = new SimpleDateFormat("hh:mm:ss"); 
                System.out.println("Executing Time for task name - "+ 
                		connector.toString() +" = " +ft.format(d));    
                // prints the execution time for every task  
            } 
            try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} 
        } 
        System.out.println(connector.toString() + " complete"); 
		*/
        
        /*
         * End of Test Code. 
         */
        
		BufferedReader input = null;
		PrintWriter output = null;
		BufferedOutputStream dataOutput = null;
		
		try {
			// read characters from the client using input stream on the socket
			input = new BufferedReader(new InputStreamReader(connector.getInputStream()));
			
			// character output stream to client (Headers, etc.)
			output = new PrintWriter(connector.getOutputStream());
			
			// binary output stream to client (for requested data)
			dataOutput = new BufferedOutputStream(connector.getOutputStream());
			
			// read client input characters
			String clientInput = input.readLine();
			StringTokenizer parser = new StringTokenizer(clientInput);
			
			String httpMethod = parser.nextToken().toUpperCase();
			String fileRequested = parser.nextToken().toUpperCase();
			
			// TODO: Only support GET method now.
			if (httpMethod.equals(Constants.GET)) {
				
				// Main web page to display
				if (fileRequested.endsWith("/")) {
					fileRequested += Constants.DEFAULT_FILE;
				}
				File file = new File(Constants.WEB_ROOT, fileRequested);
				
				// we send HTTP Headers with data to client
				sendResponse(
						output,
						dataOutput,
						file,
						Constants.OK_RESPONSE,
						getContentType(fileRequested));
				
				System.out.println("File " + fileRequested + " returned");
				
			} else {
				
				System.out.println(Constants.NOT_IMPLEMENTED + " method: " + httpMethod);
				// Return the not implemented web page apologizing to the client
				File file = new File(Constants.WEB_ROOT, Constants.METHOD_NOT_SUPPORTED);
				// HTTP Headers with data to client
				sendResponse(
						output,
						dataOutput,
						file,
						Constants.NOT_IMPLEMENTED,
						"text/html");
			}
			
		} catch (FileNotFoundException fnfe) {
			System.out.println("File not found: " + fnfe.getMessage());
			try {
				File file = new File(Constants.WEB_ROOT, Constants.FILE_NOT_FOUND);
				sendResponse(
						output,
						dataOutput,
						file,
						Constants.OK_RESPONSE,
						"text/html");
			} catch (IOException ioe) {
				System.out.println("Problem Reading the file. " + ioe.getMessage());
			}
		} catch (IOException ioe) {
			System.err.println("Server Error: " + ioe.getMessage());
		} finally {
			// Cleanup resources in case of exception
			try {
				input.close();
				output.close();
				dataOutput.close();
				connector.close();
			} catch (IOException e) {
				System.err.println("Error closing stream : " + e.getMessage());
			}
			System.out.println("Connection closed.\n");
		}
	}
	
	private void sendResponse(PrintWriter out, BufferedOutputStream dataStream, File file, String response_type, String content_type) throws IOException {
		byte [] fileData = Utils.readFileData(file);
		out.println(response_type);
		out.println("Date: " + new Date());
		out.println("Content-type: " + content_type);
		out.println("Content-length: " + (int)file.length());
		out.println(); // blank line between headers and content is very important
		out.flush();
		
		dataStream.write(fileData, 0, (int)file.length());
		dataStream.flush();
	}
	
	private String getContentType(String fileRequested) {
		if (fileRequested.endsWith(".html") || fileRequested.endsWith("htm"))
			return "text/html";
		else
			return "text/plain";
	}
}
