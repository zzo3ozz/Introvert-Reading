package struct;

import java.util.ArrayList;
import java.util.HashMap;
import java.time.LocalDate;

public class team {
	private int r_num;
	private int t_id;
	private LocalDate start;
	private LocalDate end;
	private ArrayList<member> members;
	private ArrayList<book> books;
	private HashMap<Integer, book> map;
	
	public team(int r_num, int t_id, ArrayList<member> members) {
		this.r_num = r_num;
		this.t_id = t_id;
		this.members = members;
	}
	
	public team(int r_num, int t_id, LocalDate start, LocalDate end, ArrayList<member> members, ArrayList<book> books, HashMap<Integer, book> map) {
		this.r_num = r_num;
		this.t_id = t_id;
		this.start = start;
		this.end = end;
		this.members = members;
		this.books = books;
		this.map = map;
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
	
	public ArrayList<book> getBooks() {
		return this.books;
	}
	
	public HashMap<Integer, book> get_map() {
		return this.map;
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
		String result = "<html><style> p {font-family: 'Gabia Maeumgyeol'; padding-bottom: 7px;}</style>";
		
		for(int i = 0; i < this.books.size(); i++) {
			result += "<p>「";
			if(books.get(i).getID() == null)
				result += "-";
			else {
				result += books.get(i).getTitle();
			}
			result += "」";
			result += ",</p>";
		}
		result = result.substring(0, result.length() - 5);
		result += "</p></html>";
		System.out.println(result);
		return result;
	}

	public String getNextMember(int my_num) {
		String name = "";
		for(int i = 0; i < members.size(); i++) {
			if (members.get(i).getNum() == my_num) {
				int index = i;
				if(i != members.size() - 1)
					index = i + 1;
				else 
					index = 0;
				name = members.get(index).getName();
				break;
			}
		}
		return name;
	}
}
