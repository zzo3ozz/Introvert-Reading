package struct;

import function.DBConnect;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class member {
	public final static int SUCCESS = 1;
	public final static int DISMATCH = 0;
	public final static int FAIL = -1;
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
	
	public static ArrayList<String> getMyRotation(int num) {
		Connection con = DBConnect.makeConnection();
		PreparedStatement pstmt = null;
		PreparedStatement pstmt1 = null;
		ArrayList<String> list = new ArrayList<String>();
		
		try {
			String sql = "select r_num, t_id, r_start, r_end from mypage where m_num=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, num);
			
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				int r_num = rs.getInt(1);
				int t_id = rs.getInt(2);
				String r_start = rs.getDate(3).toString();
				String r_end = rs.getDate(4).toString();
				
				String mem_sql = "select m_name from rotation_view where r_num=? and t_id=?";
				pstmt1 = con.prepareStatement(mem_sql);
				pstmt1.setInt(1, r_num);
				pstmt1.setInt(2, t_id);
				ResultSet rs1 = pstmt1.executeQuery();
				String members = "";
				
				while(rs1.next()) {
					members += (rs1.getString(1) + ", ");
				}
				members = members.substring(0, members.length() - 2);
				
				String result = Integer.toString(r_num) + "/" + Integer.toString(t_id) + "/" + r_start + "/" + r_end + "/" + members;
				list.add(result);
			}
			rs.close();			
		} catch(SQLException e) {
			e.printStackTrace();
		}
		if(pstmt != null) try{ pstmt.close();} catch(SQLException e){};
		if(pstmt1 != null) try{ pstmt1.close();} catch(SQLException e){};
		if(con != null) try{ con.close();} catch(SQLException e){};
		return list;
	}
	
	public int changePass(String nowPW, String newPW) {
		Connection con = DBConnect.makeConnection();
		PreparedStatement pstmt = null;
		int result;
		
		try {// DB에 저장된 pw값 가져오기
			String select = "select pw from Member where m_num = ?";
			

			pstmt = con.prepareStatement(select);
			pstmt.setInt(1, this.num);
			ResultSet rs = pstmt.executeQuery();
			
			if(rs.next()) {
				String dbPW = rs.getString(1);
				

				if(nowPW.equals(dbPW)) {
					String update = "update Member set pw = ? where m_num = ?";
					
					pstmt = con.prepareStatement(update);
					pstmt.setString(1, newPW);
					pstmt.setInt(2, this.num);
					
					int i = pstmt.executeUpdate();
					
					if (i == 1) {
						System.out.println("갱신 성공!");
						result = SUCCESS;
					} else {
						result = FAIL;
					}
				} else {
					System.out.print("비밀번호가 일치하지 않습니다.");
					result =  DISMATCH;
				}
			} else {
				result = FAIL;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("갱신 실패!");
			result = FAIL;
		}
		
		if(pstmt != null) try{ pstmt.close();} catch(SQLException e){};                   
        if(con != null) try{ con.close();} catch(SQLException e){};
        
        return result;
	}
}
