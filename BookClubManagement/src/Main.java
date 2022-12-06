import function.*;
import struct.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Main extends JFrame {
	public static Container c = null;
	public static login_panel login_pane = new login_panel();
	public static menu menu_pane = null;
	public static rotation_panel rotation_pane = null;
	//public static book_list book_pane = null;
	public static mypage mypage_pane = null;
	public static admin admin_pane = null;
	
	public Main() {
		// 기본 창 크기 및 설정
		setTitle("내향적 책읽기");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getRootPane().setPreferredSize(new Dimension(1000, 650));
		pack();
		setResizable(false);
		setLocationRelativeTo(null);
		
		c = getContentPane();
		c.setLayout(null);
		
		login_pane.addComponentListener(new loginClose());
		c.add(login_pane);
		
		setVisible(true);
	}
	
	public class loginClose extends ComponentAdapter{
		public void componentHidden(ComponentEvent e) {
			Component com = e.getComponent();
			c.remove(com);
			menu_pane = new menu();
			menu_pane.addComponentListener(new menuHidden());
			c.add(menu_pane);
		}
	}
	
	public class menuHidden extends ComponentAdapter{
		public void componentHidden(ComponentEvent e) {
			if(menu_pane.selected == 0) {
				rotation_pane = new rotation_panel();
				c.add(rotation_pane);
				c.repaint();
			} else if(menu_pane.selected == 1) {
				//book_pane = new book_list();
				//c.add(book_pane);
				c.repaint();
			} else if(menu_pane.selected == 2) {
				mypage_pane = new mypage();
				mypage_pane.addComponentListener(new hide_submenu());
				c.add(mypage_pane);
			} else if(menu_pane.selected == 3){
				admin_pane = new admin();
				admin_pane.addComponentListener(new hide_submenu());
				c.add(admin_pane);
			} else {
				menu_pane.setVisible(true);
			}
		}
	}
	
	public class hide_submenu extends ComponentAdapter {
		public void componentHidden(ComponentEvent e) {
			c.repaint();
		}
	}
	
	public static void main(String args[]) {
		new Main();
	}
}
