import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
//import java.util.Scanner;

public class DocumentManager {
	Connection conn;
	Statement st;
	PreparedStatement ps;
	ResultSet rs;
	FileOutputStream output;
	
	public DocumentManager(){
		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost/201Final","root","#Itunes123");
			st = conn.createStatement();			
		} catch(SQLException sqle){
			System.out.println ("SQLException: " + sqle.getMessage());
		}
	}
	
	public void AddDocument(String fileName) throws FileNotFoundException{
		try {
			File file = new File(fileName);
			if(!file.canRead()){
				throw new FileNotFoundException("Error: could not find file to write.");
			}
			
			if(CheckDocExists(fileName)){
				return;
			}
			
			FileInputStream fileInput = new FileInputStream(file);
			ps = conn.prepareStatement("INSERT INTO DocumentManager(docName, docContent) VALUES ('" + fileName + "',?)");
			ps.setBinaryStream(1,fileInput);
			ps.executeUpdate();
		} catch (SQLException e) {
			System.out.println ("SQLException: " + e.getMessage());
		}
	}
	public boolean CheckDocExists(String fileName) throws SQLException{
		ps = conn.prepareStatement("SELECT EXISTS(SELECT * FROM DocumentManager WHERE docName='" + fileName +"')");
		rs = ps.executeQuery();

		rs.next();
		if(rs.getBoolean(1)){
			System.out.println("Document already exists");
			return true;
		}
		return false;
	}
	public void UpdateDocument(String fileName) throws FileNotFoundException{
		File file = new File(fileName);
		if(!file.canRead()){
			throw new FileNotFoundException("Could not find file to write");
		}
		try {
			if(!CheckDocExists(fileName)){
				return;
			}
			ps = conn.prepareStatement("UPDATE DocumentManager SET docContent = ? WHERE docName = '" + fileName + "'");
			ps.setBinaryStream(1,new FileInputStream(file));
			ps.executeUpdate();
		} catch (SQLException e) {
			System.out.println ("SQLException: " + e.getMessage());
		}
	}
	public void ReadDocument(String fileName) throws FileNotFoundException{
		try {
			ps = conn.prepareStatement("SELECT docContent FROM DocumentManager WHERE docName = '" + fileName + "'");
			rs = ps.executeQuery();
			if(rs.next()){
				InputStream input = rs.getBinaryStream("docContent");
				byte[] buffer = new byte[1024];
				File file = new File("newFile.rtf");
	            output = new FileOutputStream(file);
                while (input.read(buffer) > 0) {
                    output.write(buffer);
                }
 			} else {
 				throw new FileNotFoundException("Could not retrieve file");
 			}
		} catch (SQLException e) {
			System.out.println ("SQLException: " + e.getMessage());
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
	public static void main (String[] args) {
		DocumentManager doc = new DocumentManager();
		try {
			doc.AddDocument("myFile.rtf");
//			doc.ReadDocument("myFile.rtf");
//			doc.UpdateDocument("myFile.rtf");
			doc.ReadDocument("myFile.rtf");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
