import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import function.rotation_f;
import function.session;
import struct.member;
import struct.rotation;

public class mypage extends JPanel {
	final int MENU_NUM = 2;
	private int selected;
	public static myRtnPane myRtn = new myRtnPane();
	
	public mypage() {
		setBackground(Color.ORANGE);
		setBounds(700, 0, 300, 700);
		setLayout(new FlowLayout());
		
		JButton[] btn_list = new JButton[MENU_NUM];
		
		btn_list[0] = new JButton("참여 기록");
		btn_list[1] = new JButton("비밀번호 변경");
		
		for(int i = 0; i < MENU_NUM; i++) {
			btn_list[i].addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					JButton btn = (JButton)e.getSource();
					
					for(int j = 0; j < MENU_NUM; j++) {
						if(btn.equals(btn_list[j])) {
							selected = j;
						}
					}
					setVisible(false);
					
					if(selected == 0) {
						Main.c.add(myRtn);
					} else {
//						Main.c.add(search);
					}
				}
				
			});
			add(btn_list[i]);
		}
	}
	
	public static class myRtnPane extends JPanel {
		public myRtnPane() {
			setBackground(Color.ORANGE);
			setBounds(70, 0, 930, 700);
			setPreferredSize(new Dimension(930, 700));
			setLayout(null);
			
			JLabel la = new JLabel("참여한 로테이션 목록");
			la.setBounds(30, 30, 200, 30);
			add(la);
			
			JPanel pan = new JPanel();
			pan.setBounds(30, 60, 850, 570);
			pan.setLayout(null);
			
			String header[] = {"로테이션 회차", "팀 구분", "로테이션 시작일", "로테이션 종료일", "구성 멤버", "예정"};
			DefaultTableModel model = new DefaultTableModel(header, 0) {
				public boolean isCellEditable(int rowIndex, int ColIndex) {
					return false;
				}
			};
			
			JTable r_list = new JTable(model);
			r_list.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			r_list.getColumn("로테이션 회차").setPreferredWidth(80);
			r_list.getColumn("팀 구분").setPreferredWidth(60);
			r_list.getColumn("로테이션 시작일").setPreferredWidth(180);
			r_list.getColumn("로테이션 종료일").setPreferredWidth(180);
			r_list.getColumn("구성 멤버").setPreferredWidth(280);
			r_list.getColumn("예정").setPreferredWidth(66);
			r_list.getTableHeader().setReorderingAllowed(false);
			r_list.getTableHeader().setResizingAllowed(false);

//			//독서기록 가져오는 구문으로 변경
//			ArrayList<rotation> rt = rotation_f.getAllRotation();
//			int rt_num = rt.size();
//			
//			ArrayList<String[]> strs = new ArrayList<String[]>();
//			ArrayList<String[]> test = rt.get(0).getColumn();
//
//			for(int i = 0; i < rt_num; i++) {
//				strs.addAll(rt.get(i).getColumn());
//			}
//			
//			for(int i = 0; i < strs.size(); i++) {
//				String[] line = strs.get(i);
//				model.addRow(line);
//			}
			
			JScrollPane list = new JScrollPane(r_list);
			list.setPreferredSize(new Dimension(850, 570));
			list.setSize(850, 570);
			
			pan.add(list);
			add(pan);
		}
	}

	public static class changePW extends JPanel {
		private JLabel error = new JLabel();
		private JPasswordField[] field = new JPasswordField[3];
		
		public changePW() {
			setBackground(Color.ORANGE);
			setBounds(700, 0, 300, 700);
			setLayout(new FlowLayout());
			
			JLabel la1 = new JLabel("현재 비밀번호");
			JLabel la2 = new JLabel("새 비밀번호");
			JLabel la3 = new JLabel("새 비밀번호 확인");
			JButton btn = new JButton("변경하기");
			
			btn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					String now_pw = String.valueOf(field[0].getPassword());
					String new_pw = String.valueOf(field[1].getPassword());
					String check_pw = String.valueOf(field[2].getPassword());
					
					if(now_pw == "")
						error.setText("현재 비밀번호를 입력하세요.");
					else if(new_pw == "")
						error.setText("새 비밀번호를 입력하세요.");
					else if(check_pw == "")
						error.setText("비밀번호 확인이 필요합니다.");
					else {
						int result = session.login_member.changePass(now_pw, new_pw);
						
						String s = "";
						if(result == member.SUCCESS) {
							
						} else if (result == member.DISMATCH) {
							
						} else {
							
						}
						
					}
				}
				
			});
			
			
			for(int i = 0; i < 3; i++)
				field[i] = new JPasswordField();
			
			
			add(la1); add(field[0]);
			add(la2); add(field[1]);
			add(la3); add(field[2]);
			add(btn);
		}
		
	}
}
