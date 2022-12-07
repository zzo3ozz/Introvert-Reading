package function;

import java.sql.*;

import struct.book;

public class book_f {
	final static int EXIST = 0;
	final static int FAIL = 1;
	final static int SUCCESS = 2;
	
	public static boolean isNew(book newBook) {
		Connection con = DBConnect.makeConnection();
		PreparedStatement pstmt = null;
		Boolean result = true;
		try {
			String check = "select 1 from book where isbn=?";
			pstmt = con.prepareStatement(check);
			pstmt.setString(1, newBook.getID());
			ResultSet rs = pstmt.executeQuery();
			
			if(rs.next()) {
				result = false; 
			}
			
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		if(pstmt != null) try{ pstmt.close();} catch(SQLException e){};
        if(con != null) try{ con.close();} catch(SQLException e){};
        
        return result;
	}
	
	public static int enrollBook(int r_num, int m_num, book newBook) {
		Connection con = DBConnect.makeConnection();
		PreparedStatement pstmt = null;
		int result = FAIL;
		try {
			con.setAutoCommit(false);
			
			String insert = "insert book (isbn, title, author, cover, genre, owner) values (?, ?, ?, ?, ?, ?)";
			pstmt = con.prepareStatement(insert);
			pstmt.setString(1, newBook.getID());
			pstmt.setString(2, newBook.getTitle());
			pstmt.setString(3, newBook.getAuthor());
			pstmt.setString(4, newBook.getCover());
			pstmt.setString(5, newBook.getGenre());
			pstmt.setInt(6, m_num);
			pstmt.executeUpdate();
			
			String update = "update tmember set isbn=? where m_num=? and r_num=?";
			pstmt = con.prepareStatement(update);
			pstmt.setString(1, newBook.getID());
			pstmt.setInt(2, m_num);
			pstmt.setInt(3, r_num);
			pstmt.executeUpdate();
			
			String update2 = "update reading set isbn=? where r_num=? and b_owner=?";
			pstmt = con.prepareStatement(update2);
			pstmt.setString(1, newBook.getID());
			pstmt.setInt(2, r_num);
			pstmt.setInt(3, m_num);
			pstmt.executeUpdate();
			
			con.commit();
			
			result = SUCCESS;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		if(pstmt != null) try{ pstmt.close();} catch(SQLException e){};
        if(con != null) try{ con.close();} catch(SQLException e){};
		
        return result;
	}
	
	public static int updateBook(String before_isbn, book newBook) {
		Connection con = DBConnect.makeConnection();
		PreparedStatement pstmt = null;
		int result = FAIL;
		try {
			String insert = "update book set isbn=?, title=?, author=?, cover=?, genre=? where isbn=?";
			pstmt = con.prepareStatement(insert);
			pstmt.setString(1, newBook.getID());
			pstmt.setString(2, newBook.getTitle());
			pstmt.setString(3, newBook.getAuthor());
			pstmt.setString(4, newBook.getCover());
			pstmt.setString(5, newBook.getGenre());
			pstmt.setString(6, before_isbn);
			pstmt.executeUpdate();
			result = SUCCESS;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		if(pstmt != null) try{ pstmt.close();} catch(SQLException e){};
        if(con != null) try{ con.close();} catch(SQLException e){};
        
        return SUCCESS;
	}
}
