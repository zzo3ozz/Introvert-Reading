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

import function.book_f;
import function.rotation_f;
import function.session;
import struct.book;

public class rotation_panel extends JPanel {
	private String myBookCoverPath;
	private book myBook;
	private now_rotation now_rotation_pane;
	private now_reading now_reading_pane;
	public rotation_panel() {
		System.out.println("rotation_pane");
		setBounds(0, 0, 1000, 650);
		setLayout(null);
		
		initSession();
		now_rotation_pane = new now_rotation();
		now_reading_pane = new now_reading();

		add(new navigation());
		
		JPanel content = new JPanel();
					
		content.setBackground(Color.ORANGE);
		content.setBounds(70, 0, 930, 650);
		content.setPreferredSize(new Dimension(930, 650));
		content.setLayout(null);
		
		content.add(now_rotation_pane);
		content.add(now_reading_pane);
		add(content);
		
		setVisible(true);
	}
	
	public class now_rotation extends JPanel {
		private BookCover cover;
		private JLabel r_num = new JLabel();
		private JLabel date = new JLabel();
		private JLabel members = new JLabel();
		private JLabel books = new JLabel();

		public now_rotation() {
			setBounds(40, 40, 350, 570);
			setLayout(new FlowLayout());
			
			JLabel title = new JLabel("현재 로테이션 정보");
			//title.setBounds(10, 10, 330, 40);
			
			if(myBookCoverPath == null) {
				// 현재 로그인한 멤버의 현재 로테이션 도서의 커버 정보가 null인 경우 
				cover = new BookCover(); //default 갖는 BookCover 생성
			} else {
				// 현재 로그인한 멤버의 현재 로테이션 도서 커버 정보가 존재하는 경우
				cover = new BookCover(myBookCoverPath);
			}
			
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
		
		public void refresh() {
			initSession();
			setInfo();
		}
		
		public void setInfo() {
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
			
			BookCover cover;
			if(myBookCoverPath == null) {
				// 현재 로그인한 멤버의 현재 로테이션 도서의 커버 정보가 null인 경우 
				cover = new BookCover(); //default 갖는 BookCover 생성
			} else {
				// 현재 로그인한 멤버의 현재 로테이션 도서 커버 정보가 존재하는 경우
				cover = new BookCover(myBookCoverPath);
			}
			JLabel isbn = new JLabel("ISBN");
			JTextField isbnField = new JTextField(15);
			JLabel title = new JLabel("제목");
			JTextField titleField = new JTextField(15);
			JLabel author = new JLabel("저자");
			JTextField authorField = new JTextField(15);
			JLabel genre = new JLabel("장르");
			JTextField genreField = new JTextField(15);
			// 표지 fileChooser 사용
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
							genreField.getText(), pathField.getText());
					if(myBook.getID() == null) {
						book_f.enrollBook(session.now_team.get_r(), session.login_member.getNum(), newBook);
					} else {
						String before_isbn = myBook.getID();
						myBook = newBook;
						book_f.updateBook(before_isbn, newBook);
					}
					dispose();
					now_rotation_pane.refresh();
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
	
	public class now_reading extends JPanel {
		public now_reading() {
			setBounds(420, 40, 470, 570);
			
			JLabel title = new JLabel("현재 읽는 책");
			//title.setBounds(10, 10, 330, 40);
			
			BookCover cover;
//			if(myBookCoverPath == null) {
//				// 현재 로그인한 멤버의 현재 로테이션 도서의 커버 정보가 null인 경우 
//				cover = new BookCover(); //default 갖는 BookCover 생성
//			} else {
//				// 현재 로그인한 멤버의 현재 로테이션 도서 커버 정보가 존재하는 경우
//				cover = new BookCover(myBookCoverPath);
//			}
			
			add(title);
		}
	}
	
	public class BookCover extends JLabel {
		private ImageIcon icon = null;
		private Image img = null;
		
		public BookCover() {
			ImageIcon temp_icon = new ImageIcon("images/default.png");
			Image temp_img = temp_icon.getImage().getScaledInstance(140, 200, Image.SCALE_SMOOTH);
			this.icon = new ImageIcon(temp_img);
			this.img = icon.getImage();
			
			setIcon(icon);
		}
		
		public BookCover(String path) {
			ImageIcon temp_icon = new ImageIcon("images/default.png");
			Image temp_img = temp_icon.getImage().getScaledInstance(140, 200, Image.SCALE_SMOOTH);
			try {
				URL url = new URL(path);
				temp_img = ImageIO.read(url);
				temp_img = temp_img.getScaledInstance(140, 200, Image.SCALE_SMOOTH);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			this.icon = new ImageIcon(temp_img);
			this.img = icon.getImage();
			setIcon(icon);
		}
	}

	public void initSession() {
		session.now_team = rotation_f.getNowTeam(session.login_member.getNum());
		if(session.now_team != null) {
			myBook = session.now_team.get_map().get(session.login_member.getNum());
			myBookCoverPath = myBook.getCover();
		}
	}
	

}
