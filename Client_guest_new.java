package test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Scanner;

import test.diff_match_patch.Patch;

public class Client_guest{ 
	
	private static String clientText = "";
	private static String clientShadow = "";
	private static Scanner scan;
	private static diff_match_patch dmp;
	private static LinkedList<test.diff_match_patch.Patch> list_of_patches_server;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    Object[] textString;
    private String docID = "def456"; 
    private String username = "amanosme@usc.ed"; 
    private String password = "sql"; 

	public Client_guest() {
		
		dmp = new diff_match_patch();
		scan = new Scanner(System.in);
		Socket s;
		String serverTextTemp;
		
		// Connect to the port
		try {
			s = new Socket("localhost", 3456);
			ois = new ObjectInputStream(s.getInputStream());
			oos = new ObjectOutputStream(s.getOutputStream());
			
			boolean failed = true; 
			
			while (failed) {
				oos.writeObject(username);
				oos.writeObject(password);
				String passed = (String) ois.readObject();
				if (passed.equals("y")) {

					failed = false; 
				}
				else {
					System.out.println(passed);
					
				}
				
				//TODO allow the user to update the email and password so they are valid
			}
			
	
			oos.writeObject(docID); 
			serverTextTemp = "";
			serverTextTemp = (String) ois.readObject(); //blocking 
			

			clientText = serverTextTemp;
			clientShadow =serverTextTemp;
			
			// If a different client updates the server text, update this client's text to reflect the change
			try {
				// reading info from server threads ois stream
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

	public static void main(String args[]) {
		Client client = new Client();
	}




