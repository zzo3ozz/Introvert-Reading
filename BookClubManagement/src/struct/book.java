package struct;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class book {
	private String id;
	private String title;
	private String author;
	private String cover;
	private String genre;
	private int owner;
	private String owner_name;
	
	public book(String id) {
		this.id = id;
	}
	
	public book(String id, String title, String author, String cover, String genre) {
		this.id = id;
		this.title = title;
		
		if(author.equals(""))
			this.author = null;
		else
			this.author = author;
		
		if(cover.equals(""))
			this.cover = null;
		else
			this.cover = cover;
		
		if(genre.equals(""))
			this.genre = null;
		else
			this.genre = genre;
	}
	
	public book(String id, String title, String author, String cover, String genre, int owner, String owner_name) {
		this.id = id;
		this.title = title;
		this.owner = owner;
		this.owner_name = owner_name;
		
		if(author.equals(""))
			this.author = null;
		else
			this.author = author;
		
		if(cover.equals(""))
			this.cover = null;
		else
			this.cover = cover;
		
		if(genre.equals(""))
			this.genre = null;
		else
			this.genre = genre;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public void setInfo(String title, String author, String cover, String genre, int owner, String owner_name) {
		this.id = id;
		this.title = title;
		this.owner = owner;
		this.owner_name = owner_name;
		
		if(author.equals(""))
			this.author = null;
		else
			this.author = author;
		
		if(cover.equals(""))
			this.cover = null;
		else
			this.cover = cover;
		
		if(genre.equals(""))
			this.genre = null;
		else
			this.genre = genre;
	}
	
	public String getID() {
		return this.id;
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public String getAuthor() {
		return this.author;
	}
	
	public String getCover() {
		return this.cover;
	}
	
	public String getGenre() {
		return this.genre;
	}
	
	public String getOwnerName() {
		return this.owner_name;
	}
}
