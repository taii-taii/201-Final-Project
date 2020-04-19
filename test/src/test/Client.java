package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Scanner;

import test.diff_match_patch.Patch;

public class Client extends Thread {
	private static String clientText = "";
	private static String clientShadow = "";
	private static Scanner scan;
	private static diff_match_patch dmp;
	private static LinkedList<test.diff_match_patch.Patch> list_of_patches_server;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    Object[] textString;

	public Client() {
		
		dmp = new diff_match_patch();
		scan = new Scanner(System.in);
		Socket s;
		
		// Connect to the port
		try {
			s = new Socket("localhost", 3456);
			ois = new ObjectInputStream(s.getInputStream());
			oos = new ObjectOutputStream(s.getOutputStream());
		} catch (IOException e) {
			System.out.println("Error in connecting port/host. Exiting program...");
			System.exit(0);
		}
		
		// Get the original server text and update the client text and client shadow
		// pull from database in actual implementation
		System.out.println("Please enter the server text: ");
		String serverTextTemp = scan.nextLine();
		clientText = serverTextTemp;
		clientShadow =serverTextTemp;
		
		// Used to check for upates from other clients
		this.start();
		
		// Continuously checks for updates to the local client text
		while(true) {
			try {
				
				// Read in the updated local client text
				while (clientText.equals(clientShadow)) {
					System.out.println("Please enter the new text");
					clientText = scan.nextLine();
				}

				// Send an array of the client shadow and the new text to the server
				String[] clientUpdates = {clientShadow, clientText};
				oos.writeObject(clientUpdates);

				clientShadow = clientText;
				
				System.out.println("The updated client text is: ");
				System.out.println(clientText);

			} catch (IOException e) {
				System.out.println("ioe in Client: " + e.getMessage());
			}
		}
	}

	public static void main(String args[]) {
		Client client = new Client();
	}

	// If a different client updates the server text, update this client's text to reflect the change
	public void run() {
		try {
			while(true) {

				// Get the server patches
				String[] serverUpdates = (String[]) ois.readObject();
				list_of_patches_server = dmp.patch_make(serverUpdates[1], serverUpdates[0]);
				
				// Update the client text and client shadow
				clientShadow = serverUpdates[0];
				textString = dmp.patch_apply(list_of_patches_server, clientText);
				clientText = (String) textString[0];
				
				System.out.println("The updated client text is: ");
				System.out.println(clientText);

			}
		} catch (IOException ioe) {
			System.out.println("ioe in Client run: " + ioe.getMessage());
		} catch (ClassNotFoundException e) {
			System.out.println("class not found in Client run: " + e.getMessage());
		}
	}
}
