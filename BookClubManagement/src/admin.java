import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.time.LocalDate;
import net.sourceforge.jdatepicker.impl.*;

import function.rotation_f;
import function.session;
import struct.rotation;
import UI.*;

public class admin extends JPanel {
	final int MENU_NUM = 2; 
	public int selected;
	private JButton[] btn_list = new JButton[MENU_NUM];
	public static searchP search;
	public static enrollP enroll;

	public admin() {
		setBackground(Colors.mid);
		setBounds(700, 0, 300, 650);
		setLayout(null);
		
		btn_list[0] = new MenuButton("새로운 로테이션 등록"); btn_list[0].setBounds(25, 240, 250, 40);
		btn_list[1] = new MenuButton("로테이션 기록"); btn_list[1].setBounds(25, 290, 250, 40);
		
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
					Main.admin_pane.setVisible(false);
					session.stored_page = session.page_name.ADMIN;
					
					if(selected == 0) {
						enroll = new enrollP();
						Main.layer.add(enroll, 1);
					} else {
						search = new searchP();
						Main.layer.add(search, 1);
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
	
	public static class enrollP extends JPanel {
		private	BackButton backBtn = new BackButton();
		public enrollP() {
			setBackground(Colors.mid);
			setBounds(700, 0, 300, 650);
			setLayout(null);
			
			JPanel pan = new JPanel();
			pan.setBounds(0, 250, 300, 250);
			pan.setPreferredSize(new Dimension(300, 250));
			pan.setBackground(null);
			pan.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
			
			JLabel la1 = new JLabel("로테이션 시작일");
			la1.setFont(Fonts.setFont(16)); la1.setForeground(Colors.base);
			
			JLabel la2 = new JLabel("로테이션 종료일");
			la2.setFont(Fonts.setFont(16)); la2.setForeground(Colors.base);
			
			JLabel la3 = new JLabel("<html><p style='text-align:center; font-family: \"Gabia Maeumgyeol\";'>시작일, 종료일을 모두 지정하지 않는 경우<br>자동 설정 됩니다.</p></html>");
			la3.setPreferredSize(new Dimension(300, 50));
			la3.setFont(Fonts.setFont(13)); la3.setForeground(Colors.base); la3.setHorizontalAlignment(JLabel.CENTER);
			
			JDatePickerImpl datePicker1 = createDatePicker();
			JDatePickerImpl datePicker2 = createDatePicker();
			
			JButton btn = new JButton("등록하기"); btn.setPreferredSize(new Dimension(140, 40));
			btn.setFont(Fonts.setFont(18)); btn.setBackground(null); btn.setForeground(Colors.base); btn.setBorder(null);
			btn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					java.util.Date temp_start = (java.util.Date) datePicker1.getModel().getValue();
					java.util.Date temp_end = (java.util.Date) datePicker2.getModel().getValue();
					
					int result = 0;
					
					if(temp_start == null && temp_end == null) 
						result = rotation_f.setRotation();
					else if (temp_start == null) {
						la3.setText("시작일을 입력하세요.");
						la3.setForeground(Color.RED);
					} else if (temp_end == null) {  
						la3.setText("종료일을 입력하세요.");
						la3.setForeground(Color.RED);
					} else  {
						LocalDate start = new java.sql.Date(temp_start.getTime()).toLocalDate();
						LocalDate end = new java.sql.Date(temp_end.getTime()).toLocalDate();
						
						result = rotation_f.setRotation(start, end);
						String message = "";
						
						if(result == rotation_f.SUCCESS) {
							JOptionPane pop = new JOptionPane();
							pop.setBackground(Colors.base);
							pop.setFont(Fonts.setFont(13));
							pop.showMessageDialog(Main.c, "등록 성공");
							backBtn.doClick();
						} else if (result == rotation_f.BEFORE_START)
							la3.setText("종료일은 시작일을 앞설 수 없습니다.");
						else if (result == rotation_f.BEFORE_END)
							la3.setText("<html><p style='text-align:center; font-family: \"Gabia Maeumgyeol\";'>새로운 로테이션 시작일은 마지막 로테이션의 종료일을<br>앞설 수 없습니다.</p></html>");
						else if (result == rotation_f.DURATION_ERROR)
							la3.setText("시작일과 종료일은 최소 4일 이상 차이가 나야 합니다.");
						else
							la3.setText("저장 실패");

						la3.setText("<html><p style='text-align:center; font-family: \"Gabia Maeumgyeol\";'>시작일, 종료일을 모두 지정하지 않는 경우<br>자동 설정 됩니다.</p></html>");
						la3.setForeground(Colors.base);
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
			
			pan.add(la1); pan.add(datePicker1);
			pan.add(la2); pan.add(datePicker2);
			pan.add(la3);
			pan.add(btn);
			
			backBtn.setBounds(10, 10, 40, 40);
			
			add(backBtn);
			add(pan);
		}
		
		public JDatePickerImpl createDatePicker() {
			UtilDateModel model = new UtilDateModel();
			JDatePanelImpl datePanel = new JDatePanelImpl(model);
			JDatePickerImpl datePicker = new JDatePickerImpl(datePanel);
			return datePicker;
		}
	}
	
	public static class searchP extends JPanel {
		public searchP() {
			setBounds(0, 0, 1000, 650);
			setLayout(null);
			setBackground(new Color(0, 0, 0, 0));
			
			add(new navigation());
			
			JPanel content = new JPanel();
						
			content.setBackground(Colors.mid);
			content.setBounds(70, 0, 930, 650);
			content.setLayout(null);
			
			JLabel la = new JLabel("로테이션 목록");
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
			r_list.getColumn("팀 구분").setPreferredWidth(70);
			r_list.getColumn("로테이션 시작일").setPreferredWidth(150);
			r_list.getColumn("로테이션 종료일").setPreferredWidth(150);
			r_list.getColumn("구성 멤버").setPreferredWidth(277);
			r_list.getTableHeader().setReorderingAllowed(false);
			r_list.getTableHeader().setResizingAllowed(false);
			
			ArrayList<rotation> rt = rotation_f.getAllRotation();
			int rt_num = rt.size();
			
			if(rt_num != 0) {
				ArrayList<String[]> strs = new ArrayList<String[]>();
	
				for(int i = 0; i < rt_num; i++) {
					strs.addAll(rt.get(i).getColumn());
				}
				
				for(int i = 0; i < strs.size(); i++) {
					String[] line = strs.get(i);
					model.addRow(line);
				}
			}
			
			JScrollPane list = new JScrollPane(r_list);
			list.setPreferredSize(new Dimension(830, 520));
			list.setSize(830, 520);
			
			pan.add(list);
			content.add(pan);
			add(content);
		}
	}
	

}

