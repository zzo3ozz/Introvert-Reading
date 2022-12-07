package struct;

import java.time.LocalDate;
import java.util.ArrayList;

public class rotation {
	public int id;
	public LocalDate startDate;
	public LocalDate endDate;
	public ArrayList<team> teams;
	
	public rotation(int id, LocalDate start, LocalDate end, ArrayList<team> teams) {
		this.id = id;
		this.startDate = start;
		this.endDate = end;
		this.teams = teams;
	}
	
	public ArrayList<String[]> getColumn() {
		ArrayList<String[]> list = new ArrayList<String[]>();

		for(int i = 0; i < this.teams.size(); i++) {
			String[] line = new String[5];
			line[0] = Integer.toString(this.id);
			line[1] = Integer.toString(this.teams.get(i).get_id());
			line[2] = this.startDate.toString();
			line[3] = this.endDate.toString();
			ArrayList<member> mem = this.teams.get(i).get_members();
			String mem_list = "";
			for(int j = 0; j < mem.size(); j++) {
				mem_list += mem.get(j).getName();
				
				if(j != mem.size() - 1) 
					mem_list += ", ";
			}
			line[4] = mem_list;
			list.add(line);
		}
		
		return list;
	}
}


