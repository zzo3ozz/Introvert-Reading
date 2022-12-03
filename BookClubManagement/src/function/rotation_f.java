package function;

import struct.*;

import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class rotation_f {
	public final static int BEFORE_START = -3;
	public final static int DURATION_ERROR = -2;
	public final static int BEFORE_END = -1;
	public final static int DB_FALSE = 0;
	public final static int SUCCESS = 1;
	
	// 오버로딩1 : 날짜 지정 없이 자동 생성
	public static int setRotation() {
		Connection con = DBConnect.makeConnection();
		PreparedStatement pstmt = null;
		PreparedStatement pstmt1 = null;
		PreparedStatement pstmt2 = null;
		
		try {		
			// 마지막 로테이션 회차 및 날짜 불러오기
			String time_sql = "select r_num, r_end from Rotation order by r_num desc limit 1";
			pstmt = con.prepareStatement(time_sql);
			ResultSet rs = pstmt.executeQuery();
			
			int r_num = 0;
			LocalDate r_date = LocalDate.now();
			
			if(rs.next()) {
				r_num = rs.getInt(1);
				r_date = rs.getDate(2).toLocalDate();
			}
			
			// 총 멤버 수 불러오기
			String mem_sql = "select COUNT(*) from Member";
			rs = pstmt.executeQuery(mem_sql);
			int num_mems = 0;
			
			if(rs.next()) {
				num_mems = rs.getInt(1);
			}
			
			rs.close();
			
			if(num_mems != 0) { 
				// 로테이션, 팀, 팀멤버 테이블은 동시에 insert 되어야 함
				con.setAutoCommit(false); 
				
				// 새로운 로테이션 회차, 날짜 설정
				r_num++;
				LocalDate new_start = r_date.plusDays(1);
				LocalDate new_end = new_start.plusMonths(4).minusDays(1);
				
				// Rotation 테이블 insert
				String new_rt = "insert Rotation values (?, ?, ?)";
				pstmt = con.prepareStatement(new_rt);
				pstmt.setInt(1, r_num);
				pstmt.setDate(2, java.sql.Date.valueOf(new_start));
				pstmt.setDate(3, java.sql.Date.valueOf(new_end));
				pstmt.executeUpdate();
				
				// random으로 3-4명의 그룹 생성 및 insert 구문
				ArrayList<int[]> group = makeGroup(num_mems);
				String team_sql = "insert Team values (?, ?)";
				String tmem_sql = "insert TMember (m_num, r_num, t_id, tm_order) values (?, ?, ?, ?)";
				String read_sql = "insert Reading (m_num, r_num, t_id, read_start, read_end) values (?, ?, ?, ?, ?)";
				
				pstmt = con.prepareStatement(team_sql);
				pstmt1 = con.prepareStatement(tmem_sql);
				pstmt2 = con.prepareStatement(read_sql);
				for(int t_id = 0; t_id < group.size(); t_id++) {	
					pstmt.setInt(1, r_num);
					pstmt.setInt(2, t_id + 1);
					pstmt.addBatch();
					pstmt.clearParameters();
					
					int group_mem = group.get(t_id).length;

					for(int tm_order = 0; tm_order < group_mem; tm_order++) {
						int m_num = group.get(t_id)[tm_order];
						pstmt1.setInt(1, m_num);
						pstmt1.setInt(2, r_num);
						pstmt1.setInt(3, t_id + 1);
						pstmt1.setInt(4, tm_order + 1);
						pstmt1.addBatch();
						pstmt1.clearParameters();
						
						// 개인 독서 기간 그룹 생성
						ArrayList<LocalDate[]> date_list = splitDuration(group_mem, new_start, new_end);
						
						for(int i = 0; i < group_mem; i++) {
							pstmt2.setInt(1, m_num);
							pstmt2.setInt(2, r_num);
							pstmt2.setInt(3, t_id + 1);
							pstmt2.setDate(4, java.sql.Date.valueOf(date_list.get(i)[0]));
							pstmt2.setDate(5, java.sql.Date.valueOf(date_list.get(i)[1]));
							pstmt2.addBatch();
							pstmt2.clearParameters();
						}
					}
				}
				
				pstmt.executeBatch();
				pstmt1.executeBatch();
				pstmt2.executeBatch();
				con.commit();
			}
			
		} catch(SQLException e) {
			System.out.print("error!");
			e.printStackTrace();
			return DB_FALSE;
		}

		if(pstmt != null) try{ pstmt.close();} catch(SQLException e){};
		if(pstmt1 != null) try{ pstmt1.close();} catch(SQLException e){}; 
		if(pstmt2 != null) try{ pstmt2.close();} catch(SQLException e){}; 
        if(con != null) try{ con.close();} catch(SQLException e){};
		return SUCCESS;
	}
	
	// 오버로딩1 : 날짜 지정 생성 
	public static int setRotation(LocalDate start, LocalDate end) {
		if(end.isBefore(start)) 
			return BEFORE_START;
		if(ChronoUnit.DAYS.between(start, end) < 3)
			return DURATION_ERROR;
		
		
		Connection con = DBConnect.makeConnection();
		PreparedStatement pstmt = null;
		PreparedStatement pstmt1 = null;
		PreparedStatement pstmt2 = null;
		
		try {		
			// 마지막 로테이션 회차 및 날짜 불러오기
			String time_sql = "select r_num, r_end from Rotation order by r_num desc limit 1";
			pstmt = con.prepareStatement(time_sql);
			ResultSet rs = pstmt.executeQuery();
			
			int r_num = 0;
			LocalDate r_date = LocalDate.now();
			
			if(rs.next()) {
				r_num = rs.getInt(1);
				r_date = rs.getDate(2).toLocalDate();
			}
			
			// 마지막 회차 로테이션 종료일보다 새로 설정한 시작일이 앞서면 false
			if(start.isBefore(r_date))
				return BEFORE_END;
			
			
			// 총 멤버 수 불러오기
			String mem_sql = "select COUNT(*) from Member";
			rs = pstmt.executeQuery(mem_sql);
			int num_mems = 0;
			
			if(rs.next()) {
				num_mems = rs.getInt(1);
			}
			
			rs.close();
			
			if(num_mems != 0) { 
				// 로테이션, 팀, 팀멤버 테이블은 동시에 insert 되어야 함
				con.setAutoCommit(false); 
				
				// 새로운 로테이션 회차, 날짜 설정
				r_num++;
				LocalDate new_start = start;
				LocalDate new_end = end.minusDays(1);
				
				// Rotation 테이블 insert
				String new_rt = "insert Rotation values (?, ?, ?)";
				pstmt = con.prepareStatement(new_rt);
				pstmt.setInt(1, r_num);
				pstmt.setDate(2, java.sql.Date.valueOf(new_start));
				pstmt.setDate(3, java.sql.Date.valueOf(new_end));
				pstmt.executeUpdate();
				
				// random으로 3-4명의 그룹 생성 및 insert 구문
				ArrayList<int[]> group = makeGroup(num_mems);
				String team_sql = "insert Team values (?, ?)";
				String tmem_sql = "insert TMember (m_num, r_num, t_id, tm_order) values (?, ?, ?, ?)";
				String read_sql = "insert Reading (m_num, r_num, t_id, read_start, read_end) values (?, ?, ?, ?, ?)";
				
				pstmt = con.prepareStatement(team_sql);
				pstmt1 = con.prepareStatement(tmem_sql);
				pstmt2 = con.prepareStatement(read_sql);
				for(int t_id = 0; t_id < group.size(); t_id++) {	
					pstmt.setInt(1, r_num);
					pstmt.setInt(2, t_id + 1);
					pstmt.addBatch();
					pstmt.clearParameters();
					
					int group_mem = group.get(t_id).length;

					for(int tm_order = 0; tm_order < group_mem; tm_order++) {
						int m_num = group.get(t_id)[tm_order];
						pstmt1.setInt(1, m_num);
						pstmt1.setInt(2, r_num);
						pstmt1.setInt(3, t_id + 1);
						pstmt1.setInt(4, tm_order + 1);
						pstmt1.addBatch();
						pstmt1.clearParameters();
						
						// 개인 독서 기간 그룹 생성
						ArrayList<LocalDate[]> date_list = splitDuration(group_mem, new_start, new_end);
						
						for(int i = 0; i < group_mem; i++) {
							pstmt2.setInt(1, m_num);
							pstmt2.setInt(2, r_num);
							pstmt2.setInt(3, t_id + 1);
							pstmt2.setDate(4, java.sql.Date.valueOf(date_list.get(i)[0]));
							pstmt2.setDate(5, java.sql.Date.valueOf(date_list.get(i)[1]));
							pstmt2.addBatch();
							pstmt2.clearParameters();
						}
					}
				}
				
				pstmt.executeBatch();
				pstmt1.executeBatch();
				pstmt2.executeBatch();
				con.commit();
			}
			
		} catch(SQLException e) {
			System.out.print("error!");
			e.printStackTrace();
			return DB_FALSE;
		}

		if(pstmt != null) try{ pstmt.close();} catch(SQLException e){};
		if(pstmt1 != null) try{ pstmt1.close();} catch(SQLException e){}; 
		if(pstmt2 != null) try{ pstmt2.close();} catch(SQLException e){}; 
        if(con != null) try{ con.close();} catch(SQLException e){};
		return SUCCESS;
	}
	
	// 모든 로테이션 정보 가져오기
	public static ArrayList<rotation> getAllRotation() {
		Connection con = DBConnect.makeConnection();
		PreparedStatement pstmt = null;
		ArrayList<rotation> result = new ArrayList<rotation>();
		
		try {
			String sql = "select r_num, t_id, m_num, m_name, r_start, r_end from rotation_view";
			pstmt = con.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			boolean haveRecord = rs.next();
			
			while(true) {
				int r_num = rs.getInt(1);
				int r_index = r_num;
				
				LocalDate r_start = rs.getDate(5).toLocalDate();
				LocalDate r_end = rs.getDate(6).toLocalDate();
				ArrayList<team> teams = new ArrayList<team>();
				
				while(r_num == r_index) {
					int t_id = rs.getInt(2);
					int t_index = t_id;
					
					ArrayList<member> members = new ArrayList<member>();
					while(t_id == t_index) {
						int m_num = rs.getInt(3);
						String m_name = rs.getString(4);
						members.add(new member(m_num, m_name));
						
						haveRecord = rs.next();
						if(haveRecord) 
							t_index = rs.getInt(2);
						else
							break;
					}
					teams.add(new team(r_num, t_id, members));
					
					if(!haveRecord)
						break;
					
					r_index = rs.getInt(1);
				}
				result.add(new rotation(r_num, r_start, r_end, teams));
				
				if(!haveRecord) {
					rs.close();
					break;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if(pstmt != null) try{ pstmt.close();} catch(SQLException e){};
		if(con != null) try{ con.close();} catch(SQLException e){};
		return result;
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
	
	public static ArrayList<int[]> makeGroup(int num_mems) {
		// 전체 회원을 3 ~ 4 명의 그룹으로 나누기
		int four_group = (num_mems / 4); // 4명인 그룹 수
		int three_group = 0; // 3명인 그룹 수
			
		if(num_mems % 4 != 0) {
			three_group = 1;
			four_group = four_group - (3 - (num_mems % 4));
		}

		int total = three_group + four_group;
		ArrayList group = new ArrayList<int[]>();
		int[] mem_list = new int[num_mems];
		
		// 1 ~ num_mems까지의 배열 생성
		for(int i = 0; i < num_mems; i++)  
			mem_list[i] = i + 1;
		
		//mem_list 셔플
		for(int i = 1; i <= num_mems; i++) {
			int random = (int) (Math.random() * (num_mems - i));
			
			int temp = mem_list[random];
			mem_list[random] = mem_list[num_mems - i];
			mem_list[num_mems - i] = temp; 
		}
		
		// 셔플된 멤버 차례로 그룹에 넣기
		int mem_list_i = 0;
		for(int i = 0; i < four_group; i++) {
			int[] temp = new int[4];
			for(int j = 0; j < 4; j++) {
				temp[j] = mem_list[mem_list_i++];
			}
			group.add(temp);
		}
		
		for(int i = 0; i < three_group; i++) {
			int[] temp = new int[3];
			for(int j = 0; j < 3; j++) {
				temp[j] = mem_list[mem_list_i++];
			}
			group.add(temp);
		}
		
		return group;
	}
	
	public static ArrayList<LocalDate[]> splitDuration(int num, LocalDate start, LocalDate end) {
		ArrayList<LocalDate[]> list = new ArrayList<LocalDate[]>();
		LocalDate pointer = start; // 포인터로 사용할 변수
		
		for(int i = 0; i < num; i++) {
			LocalDate[] temp = new LocalDate[2];
			if(num == 4 && ChronoUnit.MONTHS.between(start, end.plusDays(1)) == 4) {
				// 시작과 끝 날짜 사이의 간격이 네 달이고, 한 팀의 멤버가 총 4명인 경우 권당 독서 기간은 한 달
				temp[0] = pointer;
				pointer = pointer.plusMonths(1);
				temp[1] = pointer.minusDays(1);
			} else { // 그 외의 경우 총 기간을 멤버수로 나누어 권당 독서 기간 설정
				int days = (int) (ChronoUnit.DAYS.between(start, end) / num);
				temp[0] = pointer;
				pointer = pointer.plusDays(days);
				if(i == num - 1) 
					temp[1] = end;
				 else 
					temp[1] = pointer.minusDays(1);
			}
			list.add(temp);
		}

		return list;
	}
	
//	public static void main(String[] args) {
//		ArrayList<rotation> rotations = rotation_f.getAllRotation();
//		System.out.println(rotations.size());
//		for(int i = 0; i < rotations.size(); i++) {
//			int r_id = rotations.get(i).id;
//			System.out.println(rotations.get(i).teams.size());
//			for(int j = 0; j < rotations.get(i).teams.size(); j++) {
//				int t_id = rotations.get(i).teams.get(j).t_id;
//				System.out.println(rotations.get(i).teams.get(j).members.size());
//				for(int k = 0; k < rotations.get(i).teams.get(j).members.size(); k++) {
//					int m_num = rotations.get(i).teams.get(j).members.get(k).num;
//					String m_name = rotations.get(i).teams.get(j).members.get(k).name;
//					
//					System.out.print(r_id + " " + t_id + " " + m_num + " " + m_name + " " + rotations.get(i).startDate + " " + rotations.get(i).endDate + "\n");
//				}
//			}
//			
//		}
//		
//	}
}
