package webservice;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	/*
	 * Server class for starting and stopping a server.
	*/
	
	//Server port
	private final int PORT;
	private ServerSocket serverConnector;
	
	public Server(int port) {
		PORT = port;
		serverConnector = null;
	}
	
	// start the server
	public void start() {
		try {
			serverConnector = new ServerSocket(PORT);
			System.out.println("Server started. \nListening for connections on port: " + PORT + "...\n");
			
		} catch (IOException ioe) {
			System.err.println("Server Connection error: " + ioe.getMessage());
		}
	}
	
	// stop the server
	public void stop() {
		if (serverConnector == null) {
			System.out.println("No Server running. Exiting ...");
			return;
		}
		
		try {
			serverConnector.close();
		} catch (IOException ioe) {
			System.err.println("Server Close error: " + ioe.getMessage());
			return;
		}
		
		serverConnector = null;
		return;
	}
	
	// Wait for next client to connect to this server.
	public Socket accept() throws IOException {
		return serverConnector.accept();
	}
}
