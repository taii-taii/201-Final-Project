package test;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.Vector;




public class Server {
	private Vector<ServerThread> serverThreads;
	private Scanner scanner;
	private String serverText;
	public Server() {
		
		try {
			scanner =  new Scanner(System.in);
			ServerSocket ss = new ServerSocket(3456);
			System.out.println("Listening on port 3456.");
			System.out.println("Please enter the initial server text");
			serverText = scanner.nextLine();
			serverThreads = new Vector<ServerThread>();    		
			while(true) {
				Socket s = ss.accept();
				System.out.println("Connection from " + s.getInetAddress());
				ServerThread st = new ServerThread(s,this,serverText);
				serverThreads.add(st);
			}
		}catch (IOException ioe) {
			System.out.println("ioe in bindToPort: " + ioe.getMessage());
		}
	}
	
	public static void main(String args[]) {
		Server server = new Server();
	}
}
