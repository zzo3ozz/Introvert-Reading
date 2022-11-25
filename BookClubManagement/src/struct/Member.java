package struct;

import function.DBConnect;

import java.sql.*;

public class Member {
	// pw의 경우 보안 issue로 필요한 순간에 db에서 받아올 것
	private int num;
	private String id;
	private String name;
	private int level;
	
	public Member(ResultSet rs) {
		try { //"select m_num, id, pw, level, m_name from Member where id = ?";
			num = rs.getInt(1);
			id = rs.getString(2);
			name = rs.getString(4);
			level = rs.getInt(5);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void changePass(String nowPW, String newPW) {
		
	}
}
