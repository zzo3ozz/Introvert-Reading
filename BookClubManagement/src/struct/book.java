package struct;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class book {
	private int id;
	private String title;
	private ArrayList<String> author;
	private String genre;
	private String cover;
	private String owner;
	
	public book(int id) {
		this.id = id;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public int getID() {
		return this.id;
	}
	
	public String getTitle() {
		return this.title;
	}
}
