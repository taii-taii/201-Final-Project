package test;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.Vector;

public class Server {
	private Vector<ServerThread> serverThreads;
	private Scanner scanner;
	public Server() {
		
		int port = 3456;
		
		// Connect to the port
		try {
			scanner =  new Scanner(System.in);
			ServerSocket ss = new ServerSocket(port);
			System.out.println("Listening on port " + port + ".");
			serverThreads = new Vector<ServerThread>();
			
			// Add every client to a new server thread
			while(true) {
				Socket s = ss.accept();
				System.out.println("Connection from " + s.getInetAddress());
				ServerThread st = new ServerThread(s, this);
				serverThreads.add(st);
			}
		} catch (IOException ioe) {
			System.out.println("ioe in bindToPort: " + ioe.getMessage());
		}
	}
	
	public static void main(String args[]) {
		Server server = new Server();
	}
	
	public synchronized void broadcast(ServerThread serverThreadOrigin) {
		for (ServerThread st : serverThreads) {
			if (st != serverThreadOrigin) {
				st.updateClientText();
			}
		}
	}
}
