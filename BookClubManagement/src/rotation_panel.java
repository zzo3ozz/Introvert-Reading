import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.NumberFormat;
import java.time.LocalDate;

import javax.imageio.ImageIO;
import javax.swing.*;

import function.bookCover;
import function.book_f;
import function.rotation_f;
import function.session;
import struct.book;

public class rotation_panel extends JPanel {
	private String myBookCoverPath;
	private book myBook;
	private now_rotation now_rotation_pane;
	private my_reading my_reading_pane;
	public rotation_panel() {
		System.out.println("rotation_pane");
		setBounds(0, 0, 1000, 650);
		setLayout(null);
		
		initSession();
		now_rotation_pane = new now_rotation();
		my_reading_pane = new my_reading();

		add(new navigation());
		
		JPanel content = new JPanel();
					
		content.setBackground(Color.ORANGE);
		content.setBounds(70, 0, 930, 650);
		content.setPreferredSize(new Dimension(930, 650));
		content.setLayout(null);
		
		content.add(now_rotation_pane);
		content.add(my_reading_pane);
		add(content);
		
		setVisible(true);
	}
	
	public class now_rotation extends JPanel {
		private JLabel cover = new JLabel();
		private JLabel r_num = new JLabel();
		private JLabel date = new JLabel();
		private JLabel members = new JLabel();
		private JLabel books = new JLabel();

		public now_rotation() {
			setBounds(40, 40, 350, 570);
			setLayout(new FlowLayout());
			
			JLabel title = new JLabel("현재 로테이션 정보");
			//title.setBounds(10, 10, 330, 40);
			
			JButton btn = new JButton("등록 / 수정");
			btn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JFrame com = (JFrame) ((JButton)e.getSource()).getTopLevelAncestor();
					BookEnroll pop = new BookEnroll(com);
					pop.setVisible(true);
				}
			});
						
			setInfo();
			
			add(title);
			add(cover);
			add(btn);
			add(r_num); add(date); add(members); add(books);
		}
				
		public void setInfo() {
			ImageIcon new_icon;
			if(myBookCoverPath == null) {
				// 현재 로그인한 멤버의 현재 로테이션 도서의 커버 정보가 null인 경우 
				new_icon = bookCover.getIcon(); //default 갖는 image 생성
			} else {
				// 현재 로그인한 멤버의 현재 로테이션 도서 커버 정보가 존재하는 경우
				new_icon = bookCover.getIcon(myBookCoverPath);
			}
			
			String nowRNum = "-";
			String nowStart = "-";
			String nowEnd = "-";
			String nowMembers = "-";
			String nowBooks = "-";
			
			if(session.now_team != null) {
				nowRNum = Integer.toString(session.now_team.get_r());
				nowStart = session.now_team.get_start().toString();
				nowEnd = session.now_team.get_end().toString();
				nowMembers = session.now_team.getMembersByString();
				nowBooks = session.now_team.getBookByString();
			}
			cover.setIcon(new_icon);
			r_num.setText("회차 : " + nowRNum);
			date.setText("기간 : " + nowStart + " ~ " + nowEnd);
			members.setText("멤버 : " + nowMembers);
			books.setText("책 : " + nowBooks);
		}
	}
	
	public class BookEnroll extends JDialog {
		public BookEnroll(JFrame parent) {
			super(parent, "책 정보", true);
			setLayout(new FlowLayout());
			setSize(350, 450);
			setResizable(false);
			
			JLabel cover;
			if(myBookCoverPath == null) {
				// 현재 로그인한 멤버의 현재 로테이션 도서의 커버 정보가 null인 경우 
				cover = new JLabel(bookCover.getIcon()); //default 갖는 BookCover 생성
			} else {
				// 현재 로그인한 멤버의 현재 로테이션 도서 커버 정보가 존재하는 경우
				cover = new JLabel(bookCover.getIcon(myBookCoverPath));
			}
			JLabel isbn = new JLabel("ISBN");
			JTextField isbnField = new JTextField(15);
			JLabel title = new JLabel("제목");
			JTextField titleField = new JTextField(15);
			JLabel author = new JLabel("저자");
			JTextField authorField = new JTextField(15);
			JLabel genre = new JLabel("장르");
			JTextField genreField = new JTextField(15);
			JLabel path = new JLabel("책 표지");
			JTextField pathField = new JTextField(15);
			JLabel pathInfo = new JLabel("이미지 URL을 입력하세요.");
			
			if(myBook.getID() != null) {
				isbnField.setText(myBook.getID());
				titleField.setText(myBook.getTitle());
				authorField.setText(myBook.getAuthor());
				genreField.setText(myBook.getGenre());
				pathField.setText(myBook.getCover());
			}
			
			add(cover);
			add(isbn); add(isbnField);
			add(title); add(titleField);
			add(title); add(titleField);
			add(author); add(authorField);
			add(genre); add(genreField);
			add(path); add(pathField); add(pathInfo);
			
			JButton btn1 = new JButton("등록 / 수정");
			btn1.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					//isbn or 제목 미 입력시 오류 메세지 추가
					String str = isbnField.getText();
					String restr = str.replaceAll("[^0-9]", "");
					
					book newBook = new book(restr,
							titleField.getText(), authorField.getText(),
							pathField.getText(), genreField.getText());
					if(myBook.getID() == null) {
						book_f.enrollBook(session.now_team.get_r(), session.login_member.getNum(), newBook);
					} else {
						String before_isbn = myBook.getID();
						myBook = newBook;
						book_f.updateBook(before_isbn, newBook);
					}
					dispose();
					initSession();
					now_rotation_pane.setInfo();
				}
				
			});
			JButton btn2 = new JButton("취소");
			btn2.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			});
			
			add(btn1); add(btn2);
		}
	}
	
	public class my_reading extends JPanel {
		private now_reading now_reading_pane;
		public my_reading() {
			setBounds(420, 40, 470, 570);
			
			JLabel title = new JLabel("현재 읽는 책");
			//title.setBounds(10, 10, 330, 40);
			
			now_reading_pane = new now_reading();
			
			add(title);
			add(now_reading_pane);
		}
		
		public class now_reading extends JPanel {
			private JLabel cover = new JLabel();
			private JLabel title = new JLabel();
			private JLabel author = new JLabel();
			private JLabel genre = new JLabel();
			private JLabel owner = new JLabel();
			public now_reading() {
				setPreferredSize(new Dimension(470, 340));
								
				String drt = "독서기간 : ";
				String next = "전달 멤버 : ";
				
				if(session.now_reading != null) {
					drt += session.now_reading.getStart().toString() + " ~ " + session.now_reading.getEnd().toString();
					if(session.now_reading.getEnd() == session.now_team.get_end()) 
						next += "-";
					 else 
						next += session.now_team.getNextMember(session.login_member.getNum());
				} else {
					drt += "-";
					next += "-";
				}
				
				JLabel duration = new JLabel(drt);
				JLabel nextMember = new JLabel(next);
				
				setInfo();
				
				add(cover);
				add(title); add(author); add(genre); add(owner);
				add(duration); add(nextMember);
			}
			
			public void setInfo() {
				String s_title = "";
				String s_author = "";
				String s_genre = "";
				String s_owner = "";
				ImageIcon icon = bookCover.getIcon();
								
				if(session.now_reading != null && session.now_reading.getBook().getID() != null) {
					book now = session.now_reading.getBook();
					s_title = now.getTitle();
					s_author = (now.getAuthor() == null ? "" : now.getAuthor());
					s_genre = (now.getGenre() == null ? "" : now.getGenre());
					s_owner = now.getOwnerName();
					
					if(now.getCover() != null)
						icon = bookCover.getIcon(now.getCover()); 
				}
				
				cover.setIcon(icon);
				title.setText(s_title);
				author.setText(s_author);
				genre.setText(s_genre);
				owner.setText("소장 : " + s_owner);
			}
		}
		
		public class next_book extends JPanel {
			private JLabel nextCover;
			private JLabel nextTitle = new JLabel();
			private JLabel nextAuthor = new JLabel();
			private JLabel nextGenre = new JLabel();
			public next_book() {
				setSize(470, 120);
				
				add(nextCover); add(nextTitle); add(nextAuthor); add(nextGenre);
			}
			
		}
		
		
		public void setInfo() {
			String str_title = "-";
			String str_author = "-";
			String str_owner = "-";
			String str_nextT = "-";
			String str_nextA = "";
			String str_nextG = "";
			
			if(session.now_reading.getBook().getID() != null) {
				str_title = session.now_reading.getBook().getTitle();
				str_author = session.now_reading.getBook().getAuthor();
				//str_owner = session.now_reading.getBook().get
			}
			
			//cover= ;
			//nextCover;
//			
//			nextTitle.setText("");
//			nextAuthor.setText("");
//			nextGenre.setText("");
		}
	}
	
	public void initSession() {
		session.now_team = rotation_f.getNowTeam(session.login_member.getNum());
		session.now_reading = rotation_f.getNowReading(session.now_team.get_r(), session.login_member.getNum());
		if(session.now_team != null) {
			myBook = session.now_team.get_map().get(session.login_member.getNum());
			myBookCoverPath = myBook.getCover();
		}
	}
	
	
}