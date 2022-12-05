package struct;

import function.DBConnect;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class member {
	// pw의 경우 보안 issue로 필요한 순간에 db에서 받아올 것
	private int num;
	private String id;
	private String name;
	private int level;
	
	public member(int num, String name) {
		this.num = num;
		this.name = name;
	}
	
	public member(ResultSet rs) {
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
	
	public int getNum() {
		return this.num;
	}
	
	public int getLevel() {
		return this.level;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void printInfo() {
		System.out.print(this.num + " " + this.id + " " + this.name + " " + this.level);
	}
	
	
}
