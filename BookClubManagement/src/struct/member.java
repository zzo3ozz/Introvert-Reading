package struct;

import function.DBConnect;

import java.sql.*;

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
	
	public int getLevel() {
		return this.level;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void printInfo() {
		System.out.print(this.num + " " + this.id + " " + this.name + " " + this.level);
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
