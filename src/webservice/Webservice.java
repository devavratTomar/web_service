package webservice;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import webservice.Server;

public class Webservice {
	/*
	 * Class for handling everything.
	 * Manage all the runnable tasks in a queue.
	 * Create a Worker thread pool for serving web pages.
	 * Reference to Server class containing server information like port, files to serve etc.
	 */
	
	// We keep the Pool size fixed for this service.
	private final int POOL_SIZE;
	
	// Boolean to keep track if the web server is running.
	private boolean isRunning;
	
	private Server server;
	private ExecutorService workerPool;
	
	public Webservice(int poolSize) {
		POOL_SIZE = poolSize;
		isRunning = false;
		
		workerPool = null;
		server = null;
	}
	
	public void startService(int port) {
		if (server == null) {
			server = new Server(port);
		}
		
		if (workerPool == null) {
			workerPool = Executors.newFixedThreadPool(POOL_SIZE);
		}
		
		isRunning = true;
		
		// start the server on the given port
		server.start();
		
		while (isRunning) {
			try {
				// server.accept() will block the current thread till a new client is connected.
				Socket s = server.accept();
				ClientHandler client = new ClientHandler(s);
				workerPool.execute(client);
			} catch (IOException ioe) {
				System.err.println("Error in accepting new client: " + ioe.getMessage());
			}
		}
		
		workerPool.shutdown();
		server.stop();
	}
	
	public void requestServiceShutdown() {
		if (isRunning) {
			isRunning = false;
		}
		return;
	}
}
