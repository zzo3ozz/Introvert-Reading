import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import function.session;

public class menu extends JPanel {
	private int MENU_NUM = 3; 
	public int selected;
	
	public menu() {
		if(session.login_member.getLevel() ==  0) {
			MENU_NUM = 4;
		}
		
		setBackground(Color.ORANGE);
		setBounds(700, 0, 300, 700);
		setLayout(new FlowLayout());
		
		JButton[] btn_list = new JButton[MENU_NUM];
		
		btn_list[0] = new JButton("로테이션 일정");
		btn_list[1] = new JButton("도서 목록");
		btn_list[2] = new JButton("마이페이지");
		
		if(MENU_NUM == 4) {
			btn_list[3] = new JButton("관리자 메뉴");
		}
		
		for(int i = 0; i < MENU_NUM; i++) {
			btn_list[i].addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					JButton btn = (JButton)e.getSource();
					
					for(int j = 0; j < MENU_NUM; j++) {
						if(btn.equals(btn_list[j])) {
							selected = j;
						}
					}
					setVisible(false);
				}
			});
			add(btn_list[i]);
		}
	}
}
