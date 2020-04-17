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

public class Client {
	private static String clientText = "";
	private static String clientShadow;
	private static Scanner scan;
	private static diff_match_patch dmp;
	private static transient LinkedList<test.diff_match_patch.Patch> list_of_patches_incoming;
	private static transient LinkedList<test.diff_match_patch.Patch> list_of_patches_outgoing;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
	public Client() {
		dmp = new diff_match_patch();
		scan = new Scanner(System.in);
		Socket s;
		try {
			s = new Socket("localhost", 3456);
			ois = new ObjectInputStream(s.getInputStream());
			oos = new ObjectOutputStream(s.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Error in connecting port/host. Exiting program...");
			System.exit(0);
			e.printStackTrace();
		}
		
		System.out.println("Please enter the initial text");
		clientText = scan.nextLine();
		clientShadow = clientText;
		String newText;
		Object[] textString;
		while(true) {
			try {
				System.out.println("Please enter the new text");
				newText = scan.nextLine();
				list_of_patches_outgoing = dmp.patch_make(clientShadow,newText);
				oos.writeObject(list_of_patches_outgoing);
				clientShadow = clientText;
				list_of_patches_incoming = (LinkedList<test.diff_match_patch.Patch>)ois.readObject();
				textString = dmp.patch_apply(list_of_patches_incoming, clientShadow);
				clientShadow = (String) textString[0];
				textString = dmp.patch_apply(list_of_patches_incoming, clientText);
				clientText = (String) textString[0];
				System.out.println("The updated client text is: ");
				System.out.println(clientText);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
	
	public static void main(String args[]) {
		Client client = new Client();
	}

}
