package struct;

import java.util.ArrayList;

public class team {
	public int r_num;
	public int t_id;
	public ArrayList<member> members;
	public ArrayList<book> books;
	
	public team(int r_num, int t_id, ArrayList<member> members) {
		this.r_num = r_num;
		this.t_id = t_id;
		this.members = members;
	}
	
	public int get_id() {
		return this.t_id;
	}
	
	public ArrayList<member> get_members() {
		return this.members;
	}
}
