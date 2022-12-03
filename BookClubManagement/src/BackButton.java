import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import function.session;

public class BackButton extends JButton {
	public BackButton() {
		setText("뒤로가기");
		addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Component com = ((Component) e.getSource()).getParent();
				
				if(com instanceof navigation) {
					com = com.getParent();
				}

				if(session.stored_page == session.page_name.MYPAGE) {
					Main.mypage_pane.setVisible(true);
					session.stored_page = session.page_name.MENU;
				} else if(session.stored_page == session.page_name.ADMIN) {
					Main.admin_pane.setVisible(true);
					session.stored_page = session.page_name.MENU;
				} else {
					Main.menu_pane.setVisible(true);
				}
				
				Main.c.remove(com);
				Main.c.repaint();
			}
		});
	}
}
