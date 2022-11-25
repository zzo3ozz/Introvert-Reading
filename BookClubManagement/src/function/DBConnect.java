package function;

import java.sql.*;

public class DBConnect {
	// 노트북에서 작업 시 localhost로 변경할 것
	public static Connection makeConnection() {
		String url = "jdbc:mysql://172.30.1.20/BookClubDB?charecterEncoding=UTF-8&&serverTimezone=UT&useSSL=false";
		String id = "BCAdmin";
		String password = "Bc!753951";
		Connection con = null;
				
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			System.out.println("driver loading success!");
			con=DriverManager.getConnection(url, id, password);
			System.out.println("database connecting success!");
		} catch(ClassNotFoundException e) {
			System.out.println("cannot find driver!");
		} catch(SQLException e) {
			System.out.println("connection failed!");
		}
		
		return con;
	}
	
	public static void main(String[] args) {
		Connection conn = makeConnection();
	}
}
