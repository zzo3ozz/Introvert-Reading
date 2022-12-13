import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

import function.*;
import struct.*;
import UI.*;

public class mypage extends JPanel {
	final int MENU_NUM = 2;
	private int selected;
	private JButton[] btn_list = new JButton[MENU_NUM];
	public static myRtnPane myRtn;
	public static changePWPane changePW;
	
	public mypage() {
		setBackground(Colors.mid);
		setBounds(700, 0, 300, 700);
		setLayout(null);
				
		btn_list = new JButton[MENU_NUM];
		btn_list[0] = new MenuButton("참여 기록"); btn_list[0].setBounds(25, 240, 250, 40);
		btn_list[1] = new MenuButton("비밀번호 변경"); btn_list[1].setBounds(25, 290, 250, 40);
		
		for(int i = 0; i < MENU_NUM; i++) {
			btn_list[i].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JButton btn = (JButton)e.getSource();
					
					for(int i = 0; i < MENU_NUM; i++) {
						if(btn.equals(btn_list[i])) {
							selected = i;
							System.out.println(selected);
						}
					}
					Main.mypage_pane.setVisible(false);
					session.stored_page = session.page_name.MYPAGE;
					
					if(selected == 0) {
						myRtn = new myRtnPane();
						Main.layer.add(myRtn, 1);
					} else {
						changePW = new changePWPane();
						Main.layer.add(changePW, 1);
					}
				}
			});
			add(btn_list[i]);
		}
		
		BackButton backBtn = new BackButton();
		backBtn.setBounds(10, 10, 40, 40);
		add(backBtn);
		
		add(new LogoutButton());
	}
	
	public static class myRtnPane extends JPanel {
		public myRtnPane() {
			setBounds(0, 0, 1000, 650);
			setLayout(null);
			setBackground(new Color(0, 0, 0, 0));
			
			add(new navigation());
			
			JPanel content = new JPanel();
						
			content.setBackground(Colors.mid);
			content.setBounds(70, 0, 930, 650);
			content.setLayout(null);
			
			JLabel la = new JLabel("참여한 로테이션 목록");
			la.setBounds(50, 30, 300, 30);
			la.setFont(Fonts.setFont(24, 1)); la.setForeground(Colors.base);
			
			content.add(la);
			
			JPanel pan = new JPanel();
			pan.setBounds(50, 80, 830, 520);
			pan.setLayout(null);
			
			String header[] = {"로테이션 회차", "팀 구분", "로테이션 시작일", "로테이션 종료일", "구성 멤버"};
			DefaultTableModel model = new DefaultTableModel(header, 0) {
				public boolean isCellEditable(int rowIndex, int ColIndex) {
					return false;
				}
			};
			
			JTable r_list = new JTable(model);
			r_list.setSize(830, 520);
			r_list.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
			r_list.getColumn("로테이션 회차").setPreferredWidth(90);
			r_list.getColumn("로테이션 시작일").setPreferredWidth(150);
			r_list.getColumn("로테이션 종료일").setPreferredWidth(150);
			r_list.getColumn("팀 구분").setPreferredWidth(70);
			r_list.getColumn("구성 멤버").setPreferredWidth(277);
			r_list.getTableHeader().setReorderingAllowed(false);
			r_list.getTableHeader().setResizingAllowed(false);

			ArrayList<String> myRL = rotation_f.getMyRotation(session.login_member.getNum());
			for(int i = 0; i < myRL.size(); i++) {
				String[] line = myRL.get(i).split("/");
				model.addRow(line);
			}

			JScrollPane list = new JScrollPane(r_list);
			list.setPreferredSize(new Dimension(830, 520));
			list.setSize(830, 520);
			
			pan.add(list);
			content.add(pan);
			add(content);
		}
	}

	public static class changePWPane extends JPanel {
		private JLabel error = new JLabel();
		private JPasswordField[] field = new JPasswordField[3];
		private BackButton backBtn = new BackButton(); 
		
		public changePWPane() {
			setBackground(Colors.mid);
			setBounds(700, 0, 300, 700);
			setLayout(null);
			
			JPanel pan = new JPanel();
			pan.setBounds(0, 250, 300, 150);
			pan.setPreferredSize(new Dimension(300, 150));
			pan.setBackground(null);
			pan.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 15));
			JLabel la1 = new JLabel("현재 비밀번호"); la1.setFont(Fonts.setFont(14)); la1.setForeground(Colors.base);
			JLabel la2 = new JLabel("새 비밀번호"); la2.setFont(Fonts.setFont(14)); la2.setForeground(Colors.base);
			JLabel la3 = new JLabel("새 비밀번호 확인"); la3.setFont(Fonts.setFont(14)); la3.setForeground(Colors.base);
			
			for(int i = 0; i < 3; i++)
				field[i] = new JPasswordField(15);
			
			error.setFont(Fonts.setFont(14)); error.setForeground(Color.RED); error.setPreferredSize(new Dimension(300, 15));
			error.setHorizontalAlignment(JLabel.CENTER);
			
			pan.add(la1); pan.add(field[0]);
			pan.add(la2); pan.add(field[1]);
			pan.add(la3); pan.add(field[2]);
			pan.add(error);
			
			JButton btn = new JButton("변경하기"); btn.setBounds(90, 400, 120, 40);
			btn.setFont(Fonts.setFont(18)); btn.setBackground(null); btn.setForeground(Colors.base); btn.setBorder(null);
			btn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String now_pw = String.valueOf(field[0].getPassword());
					String new_pw = String.valueOf(field[1].getPassword());
					String check_pw = String.valueOf(field[2].getPassword());
					
					if(now_pw.equals("")) {
						error.setText("현재 비밀번호를 입력하세요.");
						field[0].requestFocus();
					} else if(new_pw.equals("")) {
						error.setText("새 비밀번호를 입력하세요.");
						field[1].requestFocus();
					} else if(check_pw.equals("")) {
						error.setText("비밀번호 확인이 필요합니다.");
						field[2].requestFocus();
					} else if(!new_pw.equals(check_pw)) {
						error.setText("새 비밀번호가 일치하지 않습니다.");
						field[2].requestFocus();
					} else {
						int result = login.changePass(session.login_member.getNum(), now_pw, new_pw);
						
						if(result == login.SUCCESS) {
							JOptionPane pop = new JOptionPane();
							pop.setBackground(Colors.base);
							UIManager.put("OptionPane.messageFont", Fonts.setFont(13));
							UIManager.put("OptionPane.buttonFont", Fonts.setFont(13));
							pop.showMessageDialog(Main.c, "변경되었습니다.");
							backBtn.doClick();
						} else if (result == login.PWERROR) {
							error.setText("등록된 비밀번호가 아닙니다.");
							field[0].requestFocus();
						} else
							error.setText("변경에 실패하였습니다.");
					}
				}
			});
			btn.addMouseListener(new MouseAdapter() {
				public void mouseEntered(MouseEvent e) {
					JButton btn = (JButton) e.getSource();
					btn.setFont(Fonts.setFont(18, 1));
					btn.setBorder(new LineBorder(Colors.base, 1, true));

				}
				
				public void mouseExited(MouseEvent e) {
					JButton btn = (JButton) e.getSource();
					btn.setFont(Fonts.setFont(18));
					btn.setBorder(null);
				}
			});
			
			backBtn.setBounds(10, 10, 40, 40);
			
			add(backBtn);
			add(pan);
			add(btn);
		}
		
	}
}
