import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;

import function.bookCover;
import function.book_f;
import function.rotation_f;
import function.session;
import struct.book;
import struct.reading;
import UI.*;

public class rotation_panel extends JPanel {
	private String myBookCoverPath;
	private book myBook;
	private now_rotation now_rotation_pane;
	private my_reading my_reading_pane;
	public rotation_panel() {
		System.out.println("rotation_pane");
		setBounds(0, 0, 1000, 650);
		setLayout(null);
		setBackground(new Color(0, 0, 0, 0));
		
		initSession();
		now_rotation_pane = new now_rotation();
		my_reading_pane = new my_reading();

		add(new navigation());
		
		JPanel content = new JPanel();
					
		content.setBackground(Colors.mid);
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
		private SmallButton btn = new SmallButton("책 등록 / 수정");
		
		public now_rotation() {
			setBounds(40, 40, 350, 570);
			setLayout(null);
			setBackground(Colors.base);
			
			JLabel title = new JLabel("현재 로테이션 정보");
			title.setBounds(15, 10, 335, 40);
			title.setFont(Fonts.setFont(24, 1)); title.setForeground(Colors.top);
			
			JLabel mybook = new JLabel("-  나의 로테이션 도서");
			mybook.setBounds(25, 60, 325, 20);
			mybook.setFont(Fonts.setFont(15)); mybook.setForeground(Colors.deep);
			
			cover.setBounds(105, 90, 140, 200);

			btn.setBounds(105, 300, 140, 25);
			btn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JFrame com = (JFrame) ((JButton)e.getSource()).getTopLevelAncestor();
					BookEnroll pop = new BookEnroll(com);
					pop.setVisible(true);
				}
			});
			
			r_num.setBounds(25, 340, 325, 20);
			r_num.setFont(Fonts.setFont(15)); r_num.setForeground(Colors.deep);
			
			date.setBounds(25, 365, 310, 20);
			date.setFont(Fonts.setFont(15)); date.setForeground(Colors.deep);
			
			members.setBounds(25, 390, 310, 20);
			members.setFont(Fonts.setFont(15)); members.setForeground(Colors.deep);
			
			JLabel booksLa = new JLabel("-  로테이션 책 :");
			booksLa.setBounds(25, 415, 105, 20);
			booksLa.setFont(Fonts.setFont(15)); booksLa.setForeground(Colors.deep);
			books.setBounds(130, 418, 200, 152);
			books.setFont(Fonts.setFont(15));
			books.setForeground(Colors.deep); books.setVerticalAlignment(JLabel.TOP);
						
			setInfo();
			
			add(title);
			add(mybook);
			add(cover);
			add(btn);
			add(r_num); add(date); add(members); add(booksLa); add(books);
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
				nowStart = session.now_team.get_start().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
				nowEnd = session.now_team.get_end().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
				nowMembers = session.now_team.getMembersByString();
				nowBooks = session.now_team.getBookByString();
			}
			cover.setIcon(new_icon);
			r_num.setText("-  현재 회차 : " + nowRNum + "회");
			date.setText("-  로테이션 기간 : " + nowStart + "~" + nowEnd);
			members.setText("-  멤버 : " + nowMembers);
			books.setText(nowBooks);
		}

	}
		
	public class my_reading extends JPanel {
		private now_reading now_reading_pane;
		private next_book next_book_pane;
		public my_reading() {
			setBounds(420, 40, 470, 570);
			setLayout(null);
			setBackground(Colors.base);
			
			JLabel title = new JLabel("현재 읽는 책");
			title.setBounds(15, 10, 335, 40);
			title.setFont(Fonts.setFont(24, 1)); title.setForeground(Colors.top);
			
			now_reading_pane = new now_reading();
			now_reading_pane.setBounds(0, 70, 470, 320);
			next_book_pane = new next_book();
			next_book_pane.setBounds(0, 390, 470, 180);
			
			add(title);
			add(now_reading_pane);
			add(next_book_pane);
		}
		
		public class now_reading extends JPanel {
			private JLabel cover = new JLabel();
			private JLabel title = new JLabel();
			private JLabel author = new JLabel();
			private JLabel owner = new JLabel();
			public now_reading() {
				setBackground(new Color(0, 0, 0, 0));
				setLayout(null);
				
				String drt = "- 독서기간 : ";
				String next = "- 전달 멤버 : ";
				
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
				
				// cover, title, author, genre, owner
				cover.setBounds(25, 0, 140, 200);
				
				title.setBounds(180, 145, 265, 30);
				title.setFont(Fonts.setFont(20, 1)); title.setForeground(Colors.deep); title.setVerticalAlignment(JLabel.BOTTOM);
				
				author.setBounds(180, 180, 265, 20);
				author.setFont(Fonts.setFont(15)); author.setForeground(Colors.deep); author.setVerticalAlignment(JLabel.BOTTOM);
				
				JLabel duration = new JLabel(drt);
				duration.setBounds(30, 230, 415, 20);
				duration.setFont(Fonts.setFont(15)); duration.setForeground(Colors.deep);
				
				JLabel nextMember = new JLabel(next);
				nextMember.setBounds(30, 260, 415, 20);
				nextMember.setFont(Fonts.setFont(15)); duration.setForeground(Colors.deep);
				
				setInfo();
				
				add(cover);
				add(title); add(author); add(owner);
				add(duration); add(nextMember);
			}
			
			public void setInfo() {
				String s_title = "";
				String s_author = "";
				String s_owner = "";
				ImageIcon icon = bookCover.getIcon();
								
				if(session.now_reading != null && session.now_reading.getBook().getID() != null) {
					book now = session.now_reading.getBook();
					s_title = now.getTitle();
					s_author = (now.getAuthor() == null ? "" : now.getAuthor());
					s_owner = now.getOwnerName();
					
					if(now.getCover() != null)
						icon = bookCover.getIcon(now.getCover());
				}
				
				cover.setIcon(icon);
				title.setText(s_title);
				author.setText(s_author);
				owner.setText("소장 : " + s_owner);
			}
		}
		
		public class next_book extends JPanel {
			private JLabel nextCover = new JLabel();
			private JLabel nextTitle = new JLabel();
			private JLabel nextAuthor = new JLabel();
			private JLabel nextDuration = new JLabel();
			public next_book() {
				// 470, 180
				setBackground(Colors.top);
				setLayout(null);
				
				setInfo();
				
				JLabel la = new JLabel("다음에 읽게될 책");
				la.setBounds(15, 10, 335, 40);
				la.setFont(Fonts.setFont(22, 1)); la.setForeground(Colors.base);
				//nextCover: 70, 100  / nextTitle, nextAuthor, nextDuration
				
				nextCover.setBounds(30, 60, 70, 100);
				
				nextTitle.setBounds(120, 80, 335, 20);
				nextTitle.setFont(Fonts.setFont(20, 1)); nextTitle.setForeground(Colors.base); nextTitle.setVerticalAlignment(JLabel.BOTTOM);
				
				nextAuthor.setBounds(120, 110, 335, 20);
				nextAuthor.setFont(Fonts.setFont(15)); nextAuthor.setForeground(Colors.base); nextAuthor.setVerticalAlignment(JLabel.BOTTOM);
				
				nextDuration.setBounds(120, 135, 335, 20);
				nextDuration.setFont(Fonts.setFont(15)); nextDuration.setForeground(Colors.base); nextDuration.setVerticalAlignment(JLabel.BOTTOM);
				
				add(la);
				add(nextCover); add(nextTitle); add(nextAuthor); add(nextDuration);
			}
			
			public void setInfo() {
				String s_title = "";
				String s_author ="";
				String s_duration = "";
				ImageIcon icon = bookCover.getIcon(70, 100);
				
				if(session.now_reading != null && session.now_reading.getNext() != null) {
					reading next = session.now_reading.getNext();
					
					if(next != null) {
						s_duration += (next.getStart() + " ~ " + next.getEnd());
						if(next.getBook().getID() != null) {
							s_title = next.getBook().getTitle();
							s_author = next.getBook().getAuthor();
							icon = bookCover.getIcon(next.getBook().getCover(), 70, 100);
						}
					}
				}
				
				nextCover.setIcon(icon);
				nextTitle.setText(s_title);
				nextAuthor.setText(s_author);
				nextDuration.setText("기간 : " + s_duration);
			}

		}
		
		public void refresh() {
			now_reading_pane.setInfo();
			next_book_pane.setInfo();
		}
	}
			
	public class BookEnroll extends JDialog {
		public BookEnroll(JFrame parent) {
			super(parent, "책 정보", true);
			setLayout(null);
			setLocationRelativeTo(null);
			getRootPane().setPreferredSize(new Dimension(350, 450));
			pack();
			setResizable(false);
			setBackground(Colors.base);
			
			JLabel cover;
			if(myBookCoverPath == null) {
				// 현재 로그인한 멤버의 현재 로테이션 도서의 커버 정보가 null인 경우 
				cover = new JLabel(bookCover.getIcon()); //default 갖는 BookCover 생성
			} else {
				// 현재 로그인한 멤버의 현재 로테이션 도서 커버 정보가 존재하는 경우
				cover = new JLabel(bookCover.getIcon(myBookCoverPath));
			}
			
			cover.setBounds(105, 10, 140, 200);
			
			JLabel isbn = new JLabel("ISBN"); isbn.setBounds(30, 230, 80, 25);
			isbn.setHorizontalAlignment(JLabel.RIGHT); isbn.setFont(Fonts.setFont(13)); isbn.setForeground(Colors.deep);
			
			JTextField isbnField = new JTextField(15); isbnField.setBounds(120, 230, 210, 25);
			isbnField.setFont(Fonts.setFont(13));
			
			JLabel title = new JLabel("제목"); title.setBounds(30, 260, 80, 25);
			title.setHorizontalAlignment(JLabel.RIGHT); title.setFont(Fonts.setFont(13)); title.setForeground(Colors.deep);
			
			JTextField titleField = new JTextField(15); titleField.setBounds(120, 260, 210, 25);
			titleField.setFont(Fonts.setFont(13));
			
			JLabel author = new JLabel("저자"); author.setBounds(30, 290, 80, 25);
			author.setHorizontalAlignment(JLabel.RIGHT); author.setFont(Fonts.setFont(13)); author.setForeground(Colors.deep);
			
			JTextField authorField = new JTextField(15); authorField.setBounds(120, 290, 210, 25);
			authorField.setFont(Fonts.setFont(13));
			
			JLabel genre = new JLabel("장르"); genre.setBounds(30, 320, 80, 25);
			genre.setHorizontalAlignment(JLabel.RIGHT); genre.setFont(Fonts.setFont(13)); genre.setForeground(Colors.deep);
			
			JTextField genreField = new JTextField(15); genreField.setBounds(120, 320, 210, 25);
			genreField.setFont(Fonts.setFont(13));
			
			JLabel path = new JLabel("책 표지");  path.setBounds(30, 350, 80, 25);
			path.setHorizontalAlignment(JLabel.RIGHT); path.setFont(Fonts.setFont(13)); path.setForeground(Colors.deep);
			
			JTextField pathField = new JTextField(15);  pathField.setBounds(120, 350, 210, 25);
			pathField.setFont(Fonts.setFont(13));
			
			JLabel pathInfo = new JLabel("이미지 url을 입력하세요.");  pathInfo.setBounds(120, 377, 210, 20);
			pathInfo.setFont(Fonts.setFont(13)); pathInfo.setForeground(Colors.deep);
			
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
			
			SmallButton btn1 = new SmallButton("등록 / 수정"); btn1.setBounds(90, 405, 80, 25);
			btn1.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String str = isbnField.getText();
					String isbn = str.replaceAll("[^0-9]", "");
					String title = titleField.getText();
					String message = "ISBN과 제목은 필수 입력 값입니다.";
					JOptionPane pop = new JOptionPane();
					pop.setBackground(Colors.base);
					UIManager.put("OptionPane.messageFont", Fonts.setFont(13));
					UIManager.put("OptionPane.buttonFont", Fonts.setFont(13));
					
					if(isbn.length() != 13 || title.equals("")) {
						if(title.equals(""))
							titleField.requestFocus();
						
						if(isbn.length() != 13) {
							isbnField.requestFocus();
							if(!str.equals(""))
								message = "ISBN 값을 잘못 입력하셨습니다. ISBN은 13자리의 숫자입니다.";
						}
						pop.showMessageDialog(((JButton)e.getSource()).getParent(), message);
						return;
					}
					
					String path = pathField.getText();
					int len = path.length();
					if(len != 0 && (path.substring(len - 3, len).equals(".jpg") || path.substring(len - 3, len).equals(".png"))) {
						pop.showMessageDialog(((JButton)e.getSource()).getParent(), "jpg 혹은 png 확장자만 등록 가능합니다.");
						pathField.requestFocus();
						return;
					}
					
					book newBook = new book(isbn, titleField.getText(), authorField.getText(), path, genreField.getText());
					if(myBook.getID() == null) {
						if(!book_f.isNew(newBook)) {
							message = "이미 등록된 도서입니다. 다른 도서를 등록하세요.";
							isbnField.requestFocus();
							pop.showMessageDialog(((JButton)e.getSource()).getParent(), message);
							return;
						}
						int result = book_f.enrollBook(session.now_team.get_r(), session.login_member.getNum(), newBook);
						if(result == book_f.SUCCESS)
							message = "등록되었습니다.";
						else
							message = "등록 실패하였습니다.";
					} else {
						String before_isbn = myBook.getID();
						myBook = newBook;
						book_f.updateBook(before_isbn, newBook);
						message = "수정되었습니다.";
					}
					
					pop.showMessageDialog(((JButton)e.getSource()).getParent(), message);
					dispose();
					initSession();
					now_rotation_pane.setInfo();
					my_reading_pane.refresh();
				}
				
			});
			SmallButton btn2 = new SmallButton("취소"); btn2.setBounds(180, 405, 80, 25);
			btn2.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			});
			
			add(btn1); add(btn2);
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