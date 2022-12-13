package function;

import java.sql.*;

public class DBConnect {
	// 노트북에서 작업 시 localhost로 변경할 것
	public static Connection makeConnection() {
		String url = "jdbc:mysql://localhost/bookclubdb?charecterEncoding=UTF-8&&serverTimezone=UT&useSSL=false";
		String id = "BCAdmin";
		String password = "Bc!753951";
		Connection con = null;
				
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con=DriverManager.getConnection(url, id, password);
		} catch(ClassNotFoundException e) {
			System.out.println("cannot find driver!");
		} catch(SQLException e) {
			System.out.println("connection failed!");
		}
		
		return con;
	}
}
