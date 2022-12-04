package struct;

import java.util.ArrayList;
import java.time.LocalDate;

public class team {
	private int r_num;
	private int t_id;
	private LocalDate start;
	private LocalDate end;
	private ArrayList<member> members;
	private ArrayList<book> books;
	
	public team(int r_num, int t_id, ArrayList<member> members) {
		this.r_num = r_num;
		this.t_id = t_id;
		this.members = members;
	}
	
	public team(int r_num, int t_id, LocalDate start, LocalDate end, ArrayList<member> members, ArrayList<book> books) {
		this.r_num = r_num;
		this.t_id = t_id;
		this.start = start;
		this.end = end;
		this.members = members;
		this.books = books;
	}
	
	public int get_r() {
		return this.r_num;
	}
	
	public int get_id() {
		return this.t_id;
	}
	
	public LocalDate get_start() {
		return this.start;
	}
	
	public LocalDate get_end() {
		return this.end;
	}
	
	public ArrayList<member> get_members() {
		return this.members;
	}
	
	public String getMembersByString() {
		String result = "";
		
		for(int i = 0; i < this.members.size(); i++) {
			result += members.get(i).getName();
			result += ", ";
		}
		result = result.substring(0, result.length() - 2);
		
		return result;
	}
	
	public String getBookByString() {
		String result = "";
		
		for(int i = 0; i < this.books.size(); i++) {
			result += books.get(i).getTitle();
			result += ", ";
		}
		result = result.substring(0, result.length() - 2);
		
		return result;
	}
}
