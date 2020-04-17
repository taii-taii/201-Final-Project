
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
	private static transient LinkedList<test.diff_match_patch.Patch> list_of_patches;
	private static String serverText;
	private String commonShadow;
	private static diff_match_patch dmp;

	public ServerThread(Socket s, Server server,String st) {
		dmp = new diff_match_patch();
		this.serverText = st;
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
		try {
			commonShadow = (String)ois.readObject();
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Object[] textString;
		while(true) {
			try {
				String newClientText = (String)ois.readObject();
				list_of_patches = dmp.patch_make(commonShadow,newClientText);
				commonShadow = newClientText;
				synchronized (ServerThread.class) {
					textString = dmp.patch_apply(list_of_patches, serverText);
				}
				String testout = dmp.patch_toText(list_of_patches);
				System.out.println(testout);
				serverText = (String) textString[0];
				System.out.println("The updated server text (on server side) is:");
				System.out.println(serverText);
				oos.writeObject(serverText);
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