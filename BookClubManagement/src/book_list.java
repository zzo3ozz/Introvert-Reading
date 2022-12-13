import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;

import function.bookCover;
import function.book_f;
import function.rotation_f;
import function.session;
import struct.book;
import struct.rotation;
import UI.*;

public class book_list extends JPanel {
	final private int MENU_NUM = 4;
	private int selected = 0;
	private JButton[] btn_list = new JButton[MENU_NUM];
	private int nowIndex = 0;
	private JPanel nowPanel;
	private currentBooks current = new currentBooks();
	private byRotation rotation = new byRotation();
	private myRecords my = new myRecords();
	private everyBooks every = new everyBooks();
	
	public book_list() {
		setBounds(0, 0, 1000, 700);
		setLayout(null);
		setBackground(new Color(0, 0, 0, 0));

		JPanel content = new JPanel();
					
		content.setBackground(Colors.mid);
		content.setBounds(70, 0, 930, 700);
		content.setPreferredSize(new Dimension(930, 700));
		content.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 25));
		
		btn_list[0] = new subBtn("현재 로테이션 도서");
		btn_list[1] = new subBtn("회차별 로테이션 도서");
		btn_list[2] = new subBtn("나의 독서 기록");
		btn_list[3] = new subBtn("전체 도서 목록");
		btn_list[0].setBackground(Colors.base); btn_list[0].setForeground(Colors.top); btn_list[0].setFont(Fonts.setFont(17, 1));
		
		for(int i = 0; i < MENU_NUM; i++) {
			content.add(btn_list[i]);
		}
		
		nowPanel = current;
		
		content.add(current); content.add(rotation); content.add(my); content.add(every);
		
		add(new navigation());
		add(content);
	}
	
	public class subBtn extends JButton {
		public subBtn(String s) {
			super(s);
			setPreferredSize(new Dimension(200, 30));
			setBackground(Colors.top); setForeground(Colors.base); setFont(Fonts.setFont(17)); 
						
			addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JButton btn = (JButton)e.getSource();
					for(int j = 0; j < MENU_NUM; j++) {
						if(btn.equals(btn_list[j])) {
							selected = j;
							break;
						}
					}
					
					if(selected == nowIndex)
						return ;
					
					btn_list[nowIndex].setBackground(Colors.top); btn_list[nowIndex].setForeground(Colors.base);
					btn_list[nowIndex].setFont(Fonts.setFont(17)); 
					
					btn.setBackground(Colors.base); btn.setForeground(Colors.top);
					btn.setFont(Fonts.setFont(17, 1)); 
					
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
			
			addMouseListener(new MouseAdapter() {
				public void mouseEntered(MouseEvent e) {
					int index = -1;
					JButton btn = (JButton)e.getSource();
					for(int j = 0; j < MENU_NUM; j++) {
						if(btn.equals(btn_list[j])) {
							index = j;
							break;
						}
					}
					if(index != selected) {
						setForeground(Colors.top);
						setFont(Fonts.setFont(17, 1));
					}
				}
				
				public void mouseExited(MouseEvent e) {
					int index = -1;
					JButton btn = (JButton)e.getSource();
					for(int j = 0; j < MENU_NUM; j++) {
						if(btn.equals(btn_list[j])) {
							index = j;
							break;
						}
					}
					if(index != selected) {
						setForeground(Colors.base);
						setFont(Fonts.setFont(17));
					}
				}
			});
		}
	
		protected void paintComponent(Graphics g) {
		    int width = getWidth();
		    int height = getHeight();

		    Graphics2D graphics = (Graphics2D) g;

		    graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		    if (getModel().isArmed()) {
		        graphics.setColor(getBackground().darker());
		    } else if (getModel().isRollover()) {
		        graphics.setColor(Colors.base);
		    } else {
		        graphics.setColor(getBackground());
		    }
		    
		    graphics.fillRoundRect(0, 0, width, height, 10, 10);

		    FontMetrics fontMetrics = graphics.getFontMetrics();
		    Rectangle stringBounds = fontMetrics.getStringBounds(this.getText(), graphics).getBounds();

		    int textX = (width - stringBounds.width) / 2;
		    int textY = (height - stringBounds.height) / 2 + fontMetrics.getAscent();

		    graphics.setColor(getForeground());
		    graphics.setFont(getFont());
		    graphics.drawString(getText(), textX, textY);
		    graphics.dispose();

		    super.paintComponent(g);
		}
	}
	
	public class currentBooks extends JPanel {
		public currentBooks() {
			setVisible(true);
			setPreferredSize(new Dimension(830, 520));
			setBackground(Colors.base);
			setLayout(new FlowLayout());
			
			BookList myTeam = new BookList(session.now_team.getBooks(), BookList.myTeam);
			//myTeam.setBounds(15, 40, 800, 200);
			myTeam.setBorder(new LineBorder(Colors.mid, 3, true));
			
			BookList others = new BookList(book_f.getOthers(session.now_team.get_r(), session.now_team.get_id()));
			JScrollPane list = new JScrollPane(others);
			list.getVerticalScrollBar().setUnitIncrement(15);
			//list.setBounds(0, 245, 830, 275);
			list.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			list.setPreferredSize(new Dimension(830, 300));
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
			setBackground(Colors.base);
			setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
			
			JLabel space1 = new JLabel("");
			space1.setPreferredSize(new Dimension(20, 50));
			JLabel space2 = new JLabel("");
			space2.setPreferredSize(new Dimension(5, 50));
			
			int last = session.now_team.get_r() - 1;
			ArrayList<String> rotation = new ArrayList<String>();
			
			for(int i = 1; i <= last; i++) {
				String index = Integer.toString(i) + "회";
				rotation.add(index);
			}
			JComboBox combo = new JComboBox(rotation.toArray(new String[rotation.size()]));
			combo.setSelectedIndex(last - 1);
			combo.setPreferredSize(new Dimension(70, 30));
			combo.setForeground(Colors.base); combo.setBackground(Colors.mid); combo.setFont(Fonts.setFont(13, 1));
			
			JButton select = new JButton("선택");
			select.setPreferredSize(new Dimension(40, 30));
			select.setFont(Fonts.setFont(15, 1)); select.setBackground(Colors.top); select.setForeground(Colors.base);
			select.setBorder(null);
			select.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					int r_num = (combo.getSelectedIndex() + 1);
					books = new BookList(book_f.getByRotation(r_num));
					list.setViewportView(books);
				}
			});
			
			select.addMouseListener(new MouseAdapter() {
				public void mouseEntered(MouseEvent e) {
					select.setBackground(Colors.mid);
				}
				
				public void mouseExited(MouseEvent e) {
					select.setBackground(Colors.top);
				}
			});
			
			books = new BookList(book_f.getByRotation(last));
			
			list = new JScrollPane(books);
			list.setBorder(null);
			list.getVerticalScrollBar().setUnitIncrement(15);
			list.setPreferredSize(new Dimension(830, 490));
			
			add(space1); add(combo); add(space2); add(select);
			add(list);
		}
		
	}
	
	public class myRecords extends JPanel {
		public myRecords() {
			setVisible(false);
			setPreferredSize(new Dimension(830, 520));
			setBackground(Colors.base);
			setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
			
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
			list.setBackground(Colors.base);
			list.setPreferredSize(new Dimension(830, 520));
			list.setSize(830, 520);
			
			add(list);
		}
	}
	
	public class everyBooks extends JPanel {
		public everyBooks() {
			setVisible(false);
			setPreferredSize(new Dimension(830, 520));
			setBackground(Colors.base);
			setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
			
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
			setPreferredSize(new Dimension(830, 230 * rows + 30));
			setLayout(new FlowLayout(FlowLayout.CENTER, 50, 30));
			setBackground(Colors.base);
			
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
			setPreferredSize(new Dimension(800, 210));
			setLayout(new FlowLayout(FlowLayout.CENTER, 50, 15));
			setBackground(Colors.base);
			
			JButton[] btn = new JButton[books.size()];
			
			for(int i = 0; i < btn.length; i++) {
				String path = books.get(i).getCover();
				ImageIcon icon = bookCover.getIcon(path, 119, 170);
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
			setLayout(null);
			setLocationRelativeTo(null);
			getRootPane().setPreferredSize(new Dimension(350, 450));
			pack();
			setResizable(false);
			setBackground(Colors.base);
			
			JLabel cover = new JLabel(bookCover.getIcon(bk.getCover())); cover.setBounds(105, 10, 140, 200);
			
			JLabel isbn = new JLabel("isbn :");isbn.setBounds(30, 230, 80, 25);
			isbn.setHorizontalAlignment(JLabel.RIGHT); isbn.setFont(Fonts.setFont(13)); isbn.setForeground(Colors.deep);
			
			JLabel isbnField = new JLabel(bk.getID()); isbnField.setBounds(120, 230, 210, 25);
			isbnField.setFont(Fonts.setFont(13));
			
			JLabel title = new JLabel("제목 :");title.setBounds(30, 260, 80, 25);
			title.setHorizontalAlignment(JLabel.RIGHT); title.setFont(Fonts.setFont(13)); title.setForeground(Colors.deep);
			
			JLabel titleField = new JLabel(bk.getTitle()); titleField.setBounds(120, 260, 210, 25);
			titleField.setFont(Fonts.setFont(13)); titleField.setForeground(Colors.deep);
			
			JLabel author = new JLabel("저자 :"); author.setBounds(30, 290, 80, 25);
			author.setHorizontalAlignment(JLabel.RIGHT); author.setFont(Fonts.setFont(13)); author.setForeground(Colors.deep);
			
			JLabel authorField = new JLabel(bk.getAuthor()); authorField.setBounds(120, 290, 210, 25);
			authorField.setFont(Fonts.setFont(13)); authorField.setForeground(Colors.deep);
			
			JLabel genre = new JLabel("장르 :"); genre.setBounds(30, 320, 80, 25);
			genre.setHorizontalAlignment(JLabel.RIGHT); genre.setFont(Fonts.setFont(13)); genre.setForeground(Colors.deep);
			
			JLabel genreField = new JLabel(bk.getGenre()); genreField.setBounds(120, 320, 210, 25);
			genreField.setFont(Fonts.setFont(13)); genreField.setForeground(Colors.deep);
			
			JLabel owner = new JLabel("소유자 :");  owner.setBounds(30, 350, 80, 25);
			owner.setHorizontalAlignment(JLabel.RIGHT); owner.setFont(Fonts.setFont(13)); owner.setForeground(Colors.deep);
			
			JLabel ownerField = new JLabel(bk.getOwnerName()); ownerField.setBounds(120, 350, 210, 25);
			ownerField.setFont(Fonts.setFont(13));  ownerField.setForeground(Colors.deep);
			
			
			SmallButton close = new SmallButton("닫기"); close.setBounds(135, 405, 80, 25);
			close.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			});
			
			add(cover);
			add(isbn); add(isbnField);
			add(title); add(titleField);
			add(title); add(titleField);
			add(author); add(authorField);
			add(genre); add(genreField);
			add(owner); add(ownerField); 
			add(close);
		}
	}
}

