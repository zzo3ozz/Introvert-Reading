import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import function.bookCover;
import function.book_f;
import function.rotation_f;
import function.session;
import struct.book;
import struct.rotation;

public class book_list extends JPanel {
	final private int MENU_NUM = 4;
	private int selected = 0;
	private int nowIndex = 0;
	private JPanel nowPanel;
	private currentBooks current = new currentBooks();
	private byRotation rotation = new byRotation();
	private myRecords my = new myRecords();
	private everyBooks every = new everyBooks();
	
	public book_list() {
		setBounds(0, 0, 1000, 700);
		setLayout(null);

		JPanel content = new JPanel();
					
		content.setBackground(Color.ORANGE);
		content.setBounds(70, 0, 930, 700);
		content.setPreferredSize(new Dimension(930, 700));
		content.setLayout(new FlowLayout());
		
		JButton[] btn_list = new JButton[MENU_NUM];
		btn_list[0] = new JButton("현재 로테이션 도서");
		btn_list[1] = new JButton("회차별 로테이션 도서");
		btn_list[2] = new JButton("나의 독서 기록");
		btn_list[3] = new JButton("전체 도서 목록");
		
		for(int i = 0; i < MENU_NUM; i++) {
			btn_list[i].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JButton btn = (JButton)e.getSource();
					for(int j = 0; j < MENU_NUM; j++) {
						if(btn.equals(btn_list[j]))
							selected = j;
					}
					
					if(selected == nowIndex)
						return ;
					
					nowPanel.setVisible(false);
					if(selected == 0) {
						nowIndex = 0;
						nowPanel = current;
						current.setVisible(true);
					} else if(selected == 1) {
						nowIndex = 1;
						nowPanel = rotation;
						rotation.setVisible(true);
					} else if(selected == 2) {
						nowIndex = 2;
						nowPanel = my;
						my.setVisible(true);
					} else {
						nowIndex = 3;
						nowPanel = every;
						every.setVisible(true);
					}
				}
			});
			content.add(btn_list[i]);
		}
		
		nowPanel = current;
		
		content.add(current); content.add(rotation); content.add(my); content.add(every);
		
		add(new navigation());
		add(content);
	}
	
	public class currentBooks extends JPanel {
		public currentBooks() {
			setVisible(true);
			setPreferredSize(new Dimension(830, 520));
			setBackground(Color.LIGHT_GRAY);
			setLayout(null);
			
			BookList myTeam = new BookList(session.now_team.getBooks(), BookList.myTeam);
			myTeam.setBounds(0, 40, 830, 200);
			
			BookList others = new BookList(book_f.getOthers(session.now_team.get_r(), session.now_team.get_id()));
			JScrollPane list = new JScrollPane(others);
			list.getVerticalScrollBar().setUnitIncrement(15);
			list.setBounds(0, 245, 830, 275);
			list.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			list.setPreferredSize(new Dimension(830, 260));
			list.setBorder(null);
			
			add(myTeam);
			add(list);
		}
	}
	
	public class byRotation extends JPanel {
		private JScrollPane list;
		private BookList books;
		public byRotation() {
			setVisible(false);
			setPreferredSize(new Dimension(830, 520));
			setBackground(Color.PINK);
			
			int last = session.now_team.get_r() - 1;
			ArrayList<String> rotation = new ArrayList<String>();
			
			for(int i = 1; i <= last; i++) {
				String index = Integer.toString(i) + "회";
				rotation.add(index);
			}
			JComboBox combo = new JComboBox(rotation.toArray(new String[rotation.size()]));
			combo.setSelectedIndex(last - 1);
			
			JButton select = new JButton("선택");
			select.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					int r_num = (combo.getSelectedIndex() + 1);
					books = new BookList(book_f.getByRotation(r_num));
					list.setViewportView(books);
				}
			});
			
			books = new BookList(book_f.getByRotation(last));
			
			list = new JScrollPane(books);
			list.getVerticalScrollBar().setUnitIncrement(15);
			list.setPreferredSize(new Dimension(830, 500));
			
			add(combo); add(select);
			add(list);
		}
		
	}
	
	public class myRecords extends JPanel {
		public myRecords() {
			setVisible(false);
			setPreferredSize(new Dimension(830, 520));
			setBackground(Color.WHITE);
			
			String header[] = {"회차", "ISBN", "책 제목", "저자", "장르", "독서 시작일", "독서 종료일", "소유자"};
			DefaultTableModel model = new DefaultTableModel(header, 0) {
				public boolean isCellEditable(int rowIndex, int ColIndex) {
					return false;
				}
			};
			
			JTable r_list = new JTable(model);
			r_list.setSize(new Dimension(830, 520));
			r_list.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
			r_list.getColumn("회차").setPreferredWidth(60);
			r_list.getColumn("ISBN").setPreferredWidth(120);
			r_list.getColumn("책 제목").setPreferredWidth(250);
			r_list.getColumn("저자").setPreferredWidth(100);
			r_list.getColumn("장르").setPreferredWidth(100);
			r_list.getColumn("독서 시작일").setPreferredWidth(130);
			r_list.getColumn("독서 종료일").setPreferredWidth(130);
			r_list.getColumn("소유자").setPreferredWidth(77);
			
			ArrayList<String> allBooks = book_f.getMyReadings(session.login_member.getNum());
			for(int i = 0; i < allBooks.size(); i++) {
				String[] line = allBooks.get(i).split("\t");
				model.addRow(line);
			}
			
			JScrollPane list = new JScrollPane(r_list);
			list.setPreferredSize(new Dimension(830, 520));
			list.setSize(830, 520);
			
			add(list);
		}
	}
	
	public class everyBooks extends JPanel {
		public everyBooks() {
			setVisible(false);
			setPreferredSize(new Dimension(830, 520));
			setBackground(Color.WHITE);
			
			String header[] = {"회차", "팀 구분", "ISBN", "책 제목", "저자", "장르", "소유자"};
			DefaultTableModel model = new DefaultTableModel(header, 0) {
				public boolean isCellEditable(int rowIndex, int ColIndex) {
					return false;
				}
			};
			
			JTable r_list = new JTable(model);
			r_list.setSize(new Dimension(830, 520));
			r_list.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
			r_list.getColumn("회차").setPreferredWidth(60);
			r_list.getColumn("팀 구분").setPreferredWidth(60);
			r_list.getColumn("ISBN").setPreferredWidth(120);
			r_list.getColumn("책 제목").setPreferredWidth(250);
			r_list.getColumn("저자").setPreferredWidth(150);
			r_list.getColumn("장르").setPreferredWidth(100);
			r_list.getColumn("소유자").setPreferredWidth(77);
			
			ArrayList<String> allBooks = book_f.getAllBooks();
			for(int i = 0; i < allBooks.size(); i++) {
				String[] line = allBooks.get(i).split("\t");
				model.addRow(line);
			}
			
			JScrollPane list = new JScrollPane(r_list);
			list.setPreferredSize(new Dimension(830, 520));
			list.setSize(830, 520);
			
			add(list);
		}
	}

	public class BookList extends JPanel {
		final static boolean myTeam = true;
		public BookList(ArrayList<book> books) {
			int rows = books.size() % 4 == 0 ? books.size() / 4 : books.size() / 4 + 1;
			setPreferredSize(new Dimension(830, 220 * rows + 20));
			setLayout(new FlowLayout(FlowLayout.CENTER, 50, 20));
			setBackground(Color.LIGHT_GRAY);
			
			JButton[] btn = new JButton[books.size()];
			
			for(int i = 0; i < btn.length; i++) {
				String path = books.get(i).getCover();
				ImageIcon icon = bookCover.getIcon(path);
				btn[i] = new JButton(icon);
				btn[i].setPreferredSize(new Dimension(140, 200));
				btn[i].addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						JButton source = (JButton)e.getSource();
						int index = -1;
						for(int i = 0; i < btn.length; i++) {
							if(source.equals(btn[i]))
								index = i;
						}
						JFrame com = (JFrame) (source.getTopLevelAncestor());
						BookInfo pop = new BookInfo(com, books.get(index));
						pop.setVisible(true);
					}
				});
				
				add(btn[i]);
			}
		}
		
		public BookList(ArrayList<book> books, Boolean myTeam) {
			setPreferredSize(new Dimension(830, 200));
			setLayout(new FlowLayout(FlowLayout.CENTER, 50, 15));
			setBackground(Color.LIGHT_GRAY);
			
			JButton[] btn = new JButton[books.size()];
			
			for(int i = 0; i < btn.length; i++) {
				String path = books.get(i).getCover();
				ImageIcon icon = bookCover.getIcon(path);
				btn[i] = new JButton(icon);
				btn[i].setPreferredSize(new Dimension(119, 170));
				btn[i].addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						JButton source = (JButton)e.getSource();
						int index = -1;
						for(int i = 0; i < btn.length; i++) {
							if(source.equals(btn[i]))
								index = i;
						}
						JFrame com = (JFrame) (source.getTopLevelAncestor());
						BookInfo pop = new BookInfo(com, books.get(index));
						pop.setVisible(true);
					}
				});
				
				add(btn[i]);
			}
		}
	}
	
	public class BookInfo extends JDialog {
		public BookInfo(JFrame parent, book bk) {
			super(parent, "책 정보", true);
			setLayout(new FlowLayout());
			setSize(350, 450);
			setResizable(false);
			
			JLabel cover = new JLabel(bookCover.getIcon(bk.getCover()));
			JLabel isbn = new JLabel("isbn : " + bk.getID());
			JLabel title = new JLabel("제목 : " + bk.getTitle());
			JLabel author = new JLabel("저자 : " + bk.getAuthor());
			JLabel genre = new JLabel("장르 : " + bk.getGenre());
			JLabel owner = new JLabel("소유자 : " + bk.getOwnerName());
			
			JButton close = new JButton("닫기");
			close.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dispose();
				}
				
			});
			
			add(cover); add(isbn); add(title); add(author); add(genre); add(owner);
			add(close);
		}
	}
}

