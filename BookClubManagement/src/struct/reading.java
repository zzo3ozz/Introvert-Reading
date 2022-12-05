package struct;

import java.time.LocalDate;
import java.util.ArrayList;

public class reading {
	private int r_num;
	private LocalDate read_start;
	private LocalDate read_end;
	private book now_book;
	private reading next_reading;
	
	public reading(int r_num, LocalDate read_start, LocalDate read_end, book now_book) {
		this.r_num = r_num;
		this.read_start = read_start;
		this.read_end = read_end;
		this.now_book = now_book;
	}
	
	public reading(int r_num, LocalDate read_start, LocalDate read_end, book now_book, reading next_reading) {
		this.r_num = r_num;
		this.read_start = read_start;
		this.read_end = read_end;
		this.now_book = now_book;
		this.next_reading = next_reading;
	}
	
	public LocalDate getStart() {
		return this.read_start;
	}
	
	public LocalDate getEnd() {
		return this.read_end;
	}
	
	public book getBook() {
		return this.now_book;
	}
}
