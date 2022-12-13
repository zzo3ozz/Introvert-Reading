package function;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

import struct.book;

public class book_f {
	public final static int EXIST = 0;
	public final static int FAIL = 1;
	public final static int SUCCESS = 2;
	
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

	public static ArrayList<String> getAllBooks() {
		ArrayList<String> result = new ArrayList<String>();
		
		Connection con = DBConnect.makeConnection();
		PreparedStatement pstmt = null;
		
		try {
			String sql = "select r_num, t_id, isbn, title, author, genre, owner_name from everybooks";
			pstmt = con.prepareStatement(sql);
			
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()) {
				String line = "";
				String r_num = Integer.toString(rs.getInt(1)); line += r_num + "\t";
				String t_id = Integer.toString(rs.getInt(2)); line += t_id + "\t";
				String isbn = rs.getString(3); line += isbn + "\t";
				String title = rs.getString(4); line += title + "\t";
				String author = rs.getString(5) == null ? "-" : rs.getString(5); line += author + "\t";
				String genre = rs.getString(6) == null ? "-" : rs.getString(6); line += genre + "\t";
				String owner_name = rs.getString(7) == null ? "-" : rs.getString(7); line += owner_name;
				
				result.add(line);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		if(pstmt != null) try{ pstmt.close();} catch(SQLException e){};
        if(con != null) try{ con.close();} catch(SQLException e){};
        
		return result;
	}

	public static ArrayList<String> getMyReadings(int m_num) {
		ArrayList<String> result = new ArrayList<String>();
		Connection con = DBConnect.makeConnection();
		PreparedStatement pstmt = null;
		LocalDate now_date = null;
		
		if(session.login_member.getNum() == 1)
			now_date = LocalDate.of(2022, 10, 3);
		else if(session.login_member.getNum() == 2)
			now_date = LocalDate.of(2023, 07, 29);
		else
			now_date = LocalDate.now();
		
		try {
			String sql = "select r_num, isbn, title, author, genre, read_start, read_end, owner_name from allreading "
					+ "where m_num=? && read_end < ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, m_num);
			pstmt.setDate(2, java.sql.Date.valueOf(now_date));
			
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()) {
				String line = "";
				String r_num = Integer.toString(rs.getInt(1)); line += r_num + "\t";
				String isbn = rs.getString(2); line += isbn + "\t";
				String title = rs.getString(3); line += title + "\t";
				String author = rs.getString(4) == null ? "-" : rs.getString(4); line += author + "\t";
				String genre = rs.getString(5) == null ? "-" : rs.getString(5); line += genre + "\t";
				String start = rs.getDate(6).toString(); line += start + "\t";
				String end = rs.getDate(7).toString(); line += end + "\t";
				String owner_name = rs.getString(8); line += owner_name;
				
				result.add(line);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		if(pstmt != null) try{ pstmt.close();} catch(SQLException e){};
        if(con != null) try{ con.close();} catch(SQLException e){};
        
		return result;
	}

	public static ArrayList<book> getByRotation(int r_num) {
		ArrayList<book> result = new ArrayList<book>();
		
		Connection con = DBConnect.makeConnection();
		PreparedStatement pstmt = null;
		
		try {
			String sql = "select isbn, title, author, cover, genre, owner, owner_name from everybooks "
					+ "where r_num=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, r_num);
			
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()) {
				String isbn = rs.getString(1);
				String title = rs.getString(2);
				String author = rs.getString(3);
				String cover = rs.getString(4);
				String genre = rs.getString(5);
				int owner = rs.getInt(6);
				String owner_name = rs.getString(7);
				
				book temp = new book(isbn, title, author, cover, genre, owner, owner_name);
				result.add(temp);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		if(pstmt != null) try{ pstmt.close();} catch(SQLException e){};
        if(con != null) try{ con.close();} catch(SQLException e){};
        
		return result;
	}
	
	public static ArrayList<book> getOthers(int r_num, int t_id) {
		ArrayList<book> result = new ArrayList<book>();
		
		Connection con = DBConnect.makeConnection();
		PreparedStatement pstmt = null;
		
		try {
			String sql = "select isbn, title, author, cover, genre, owner, owner_name from everybooks "
					+ "where r_num=? and t_id!=? ";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, r_num);
			pstmt.setInt(2, t_id);
			
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()) {
				String isbn = rs.getString(1);
				String title = rs.getString(2);
				String author = rs.getString(3);
				String cover = rs.getString(4);
				String genre = rs.getString(5);
				int owner = rs.getInt(6);
				String owner_name = rs.getString(7);
				
				book temp = new book(isbn, title, author, cover, genre, owner, owner_name);
				result.add(temp);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		if(pstmt != null) try{ pstmt.close();} catch(SQLException e){};
        if(con != null) try{ con.close();} catch(SQLException e){};
        
		return result;
	}
}
