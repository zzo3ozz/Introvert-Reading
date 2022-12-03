package function;

import struct.member;
import java.sql.*;

public class login {
	public final static int SUCCESS = 1;
	public final static int IDERROR = 0;
	public final static int PWERROR = -1;
	public final static int ERROR = -2;
	
	public static int tryLogin(String id, String pw) {
		Connection con = DBConnect.makeConnection(); 
		PreparedStatement pstmt = null;
		int result;
		
		try {
			String sql = "select m_num, id, pw, m_name, level from Member where id = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, id);
			
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				String dbPW = rs.getString(3);

				if(dbPW.equals(pw)) {
					// ID 존재, PW 일치할 경우
					member mem = new member(rs);
					
					rs.close();
					pstmt.close();
					con.close();
					
					session.login_member = mem;
					result = SUCCESS;
				} else {
					result = PWERROR;
				}
				
			} else {
				result = IDERROR;
			}
		} catch(SQLException e) {
			result = ERROR;
		}

		if(pstmt != null) try{ pstmt.close();} catch(SQLException e){};                   
        if(con != null) try{ con.close();} catch(SQLException e){};
        
		return result;
	}

	public static int changePass(int r_num, String nowPW, String newPW) {
		Connection con = DBConnect.makeConnection();
		PreparedStatement pstmt = null;
		int result;
		
		try {// DB에 저장된 pw값 가져오기
			String select = "select pw from Member where m_num = ?";
			pstmt = con.prepareStatement(select);
			pstmt.setInt(1, r_num);
			ResultSet rs = pstmt.executeQuery();
			
			if(rs.next()) {
				String dbPW = rs.getString(1);

				if(nowPW.equals(dbPW)) {
					String update = "update Member set pw = ? where m_num = ?";
					
					pstmt = con.prepareStatement(update);
					pstmt.setString(1, newPW);
					pstmt.setInt(2, r_num);
					
					int i = pstmt.executeUpdate();
					
					if (i == 1) {
						result = SUCCESS;
					} else {
						result = ERROR;
					}
				} else {
					result =  PWERROR;
				}
			} else {
				result = ERROR;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			result = ERROR;
		}
		
		if(pstmt != null) try{ pstmt.close();} catch(SQLException e){};                   
        if(con != null) try{ con.close();} catch(SQLException e){};
        
        return result;
	}
}
