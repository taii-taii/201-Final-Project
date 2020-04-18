package test;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.LinkedList;

public class ServerThread extends Thread {
	private Server server;
	private Socket socket;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private static String serverText = "";
	private transient LinkedList<test.diff_match_patch.Patch> list_of_patches_client;
	private String serverShadow = "";
	private static diff_match_patch dmp;

	public ServerThread(Socket s, Server server) {
		dmp = new diff_match_patch();
		this.server = server;
		this.socket = s;
		try {
			oos = new ObjectOutputStream(s.getOutputStream());
			ois = new ObjectInputStream(s.getInputStream());
		} catch (IOException e) {
			System.out.println("ioe in ServerThread: " + e.getMessage());
		}
		this.start();
	}

	public void run() {
		
		Object[] textString;
		
		// Continuouly wait for updates from the client and properly patch the server and other client texts
		while(true) {
			try {
				
				// Get the client shadow and new client text
				String[] clientUpdates = (String[])ois.readObject();
				
				// Calculate the patches on the client side
				list_of_patches_client = dmp.patch_make(clientUpdates[0], clientUpdates[1]); // comparing "hello" to "hey" = -llo +y
				
				System.out.println("46 Patches: " + dmp.patch_toText(list_of_patches_client));
				
				// Apply the patches to the server shadow
				textString = dmp.patch_apply(list_of_patches_client, serverShadow);
				serverShadow = (String) textString[0]; //hey
				
				System.out.println("52 serverShadow: " + serverShadow);
				System.out.println("53 serverText: " + serverText);
				
//				// Send the server text and updated server shadow to the client
//				String[] serverUpdates = {serverText, serverShadow}; //hello and hey
//				oos.writeObject(serverUpdates);
				
				// Update the server text
				synchronized (Server.class) {
					textString = dmp.patch_apply(list_of_patches_client, serverText);
					serverText = (String) textString[0];
				}
				serverShadow = serverText;
				
				System.out.println("The updated server text (on server side) is:");
				System.out.println(serverText);
				
				// Broadcast to all other clients about the updated server text
				server.broadcast(this);
							
			} catch (ClassNotFoundException e) {
				System.out.println("class not found in ServerThread: " + e.getMessage());
			} catch (IOException e) {
				System.out.println("ioe in ServerThread: " + e.getMessage());
				System.exit(1);
			}
		}
	}
	
	public void updateClientText() {
		try {
			String[] serverUpdates = {serverText, serverShadow};
			oos.writeObject(serverUpdates);
			serverShadow = serverText;
		} catch (IOException e) {
			System.out.println("ioe in ServerThread updateClientText: " + e.getMessage());
		}
	}
}