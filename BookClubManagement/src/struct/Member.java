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
	
	public void printInfo() {
		System.out.print(this.num + " " + this.id + " " + this.name + " " + this.level);
	}
	
	public void changePass(String nowPW, String newPW) {
		Connection con = DBConnect.makeConnection();
		PreparedStatement pstmt = null;
		
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
					}
				} else {
					System.out.print("비밀번호가 일치하지 않습니다.");
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("갱신 실패!");
		}
		
		if(pstmt != null) try{ pstmt.close();} catch(SQLException e){};                   
        if(con != null) try{ con.close();} catch(SQLException e){};
	}
}
