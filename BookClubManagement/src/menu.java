import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.LineBorder;

import function.session;
import UI.*;

public class menu extends JPanel {
	private int MENU_NUM = 3; 
	private JButton[] btn_list; 
	public int selected;
	
	public menu() {
		if(session.login_member.getLevel() ==  0) {
			MENU_NUM = 4;
		}
		
		setBackground(Colors.mid);
		setBounds(700, 0, 300, 650);
		setLayout(null);
		
		btn_list = new JButton[MENU_NUM];
		btn_list[0] = new MenuButton("로테이션 일정"); btn_list[0].setBounds(25, 235, 250, 40);
		btn_list[1] = new MenuButton("도서 목록"); btn_list[1].setBounds(25, 280, 250, 40);
		btn_list[2] = new MenuButton("마이페이지"); btn_list[2].setBounds(25, 325, 250, 40);
		
		if(MENU_NUM == 4) {
			btn_list[3] = new MenuButton("관리자 메뉴"); btn_list[3].setBounds(25, 370, 250, 40);
		}
		
		for(int i = 0; i < MENU_NUM; i++) {
			btn_list[i].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JButton btn = (JButton)e.getSource();
					
					for(int j = 0; j < MENU_NUM; j++) {
						if(btn.equals(btn_list[j]))
							selected = j;
					}
					
					Main.menu_pane.setVisible(false);
					session.stored_page = session.page_name.MENU;
				}
			});
			add(btn_list[i]);
		}
		
		add(new LogoutButton());
	}
}
