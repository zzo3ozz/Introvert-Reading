package function;

import struct.Member;
import java.sql.*;

public class LoginFunc {
	public static Member login(String id, String pw) {
		Connection con = DBConnect.makeConnection(); 
		PreparedStatement pstmt = null;
		
		try {
			String sql = "select m_num, id, pw, m_name, level from Member where id = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, id);
			
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				String dbPW = rs.getString(3);

				if(dbPW.equals(pw)) {
					// ID 존재, PW 일치할 경우 member return
					Member mem = new Member(rs);
					
					rs.close();
					pstmt.close();
					con.close();
					
					return mem;
					
				}
				System.out.println("비밀번호가 일치하지 않습니다.");
				
			} else {
				System.out.print("아이디가 존재하지 않습니다.");
			}
		} catch(SQLException e) {
			System.out.print("error!");
		}

		if(pstmt != null) try{ pstmt.close();} catch(SQLException e){};                   
        if(con != null) try{ con.close();} catch(SQLException e){};
        
		return null;
	}
}
