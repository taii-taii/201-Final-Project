
package test;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Map;
import java.util.Vector;


public class ServerThread extends Thread{
	private Server server;
	private Socket socket;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
	private static transient LinkedList<test.diff_match_patch.Patch> list_of_patches_incoming;
	private static transient LinkedList<test.diff_match_patch.Patch> list_of_patches_outgoing;
	private static String serverText;
	private String serverShadow;
	private static diff_match_patch dmp;

	public ServerThread(Socket s, Server server) {
		dmp = new diff_match_patch();
		this.serverText = "";
		this.server = server;
		this.socket = s;
		try {
			oos = new ObjectOutputStream(s.getOutputStream());
			ois = new ObjectInputStream(s.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.start();
	}

	public void run() {
		Object[] textString;
		while(true) {
			try {				
				list_of_patches_incoming = (LinkedList<test.diff_match_patch.Patch>)ois.readObject();				
				textString = dmp.patch_apply(list_of_patches_incoming, serverShadow);
				serverShadow = (String) textString[0];
				list_of_patches_outgoing = dmp.patch_make(serverText, serverShadow);
				oos.writeObject(list_of_patches_outgoing);
				synchronized (ServerThread.class) {
					textString = dmp.patch_apply(list_of_patches_incoming, serverText);
					serverText = (String) textString[0];
				}
				serverShadow = serverText;
				System.out.println("The updated server text (on server side) is:");
				System.out.println(serverText);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	}