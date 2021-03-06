import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Login {
	Connection conn;
	Statement st;
	PreparedStatement ps;
	ResultSet rs;
	
	public Login(){
		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost/201Final","root","#Itunes123");
			st = conn.createStatement();			
		} catch(SQLException sqle){
			System.out.println ("SQLException: " + sqle.getMessage());
		}
	}
	
	public void ResetPassword(String username, String newPassword) throws SQLException{
		if(!VerifyUser(username)){
			System.out.println("User doesn't exist.");
			return;
		} else {
			st.executeUpdate("UPDATE UserInfo SET password='" + newPassword + "' WHERE username='" + username + "'");
		}
	}
	
	public void CreateUser(String username, String password) throws SQLException{
		if(VerifyUser(username)){
			System.out.println("User already exists.");
			return;
		}
		st.executeUpdate("INSERT INTO UserInfo VALUES ('" + username +"','" +password + "')");
	}
	
	public boolean CheckPassword(String username,String password) throws SQLException{
		if(VerifyUser(username)){
			ps = conn.prepareStatement("SELECT * FROM UserInfo WHERE username='" + username + "'");
			rs = ps.executeQuery();
			rs.next();
			if(rs.getString("password").equals(password)){
				return true;
			}
		}
		return false;
	}
	
	public boolean VerifyUser(String username) throws SQLException{				
		ps = conn.prepareStatement("SELECT EXISTS(SELECT * FROM UserInfo WHERE username='" + username +"')");
		rs = ps.executeQuery();

		rs.next();
		if(rs.getBoolean(1)){
			return true;
		}
		return false;
	}
	
	public static void main (String[] args) {
		String username;
		String password;
		
		//Get info
		Scanner scanner = new Scanner(System.in);
		System.out.print("Please enter your username: ");
		username = scanner.nextLine();
		System.out.print("Please enter your password: ");
		password = scanner.nextLine();
		System.out.println();
		
		Login login = new Login();
		
		Integer userChoice = null;
		
		//Execute
		try{
			System.out.print("Enter choice: \n1) Login\n2) Reset password\n3) Create new user\n");
			userChoice = scanner.nextInt();
			System.out.println();
			
			switch(userChoice){
			case 1:
				if(login.CheckPassword(username,password)){
					System.out.println("Successfully logged in.");
				} else {
					System.out.println("Username or password do not match.");
				}
				System.out.println();
				break;
			case 2:
				System.out.print("Enter in a new password: ");
				password = scanner.nextLine();
				
				login.ResetPassword(username,password);
				System.out.println();
				break;
			case 3:
				login.CreateUser(username, password);
				break;
			}
		} catch(SQLException sqle){
			System.out.println ("SQLException: " + sqle.getMessage());
		}
		
		//Close resources
		try {
			if (login.rs != null) {
				login.rs.close();
			}
			if (login.st != null) {
				login.st.close();
			}
			if (login.ps != null) {
				login.ps.close();
			}
			if (login.conn != null) {
				login.conn.close();
			}
		} catch (SQLException sqle) {
			System.out.println("sqle: " + sqle.getMessage());
		}
		scanner.close();
		
	}
}

