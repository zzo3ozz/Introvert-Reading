import function.*;
import struct.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Main extends JFrame {
	static Container c;
	static login_panel login_pane = new login_panel();
	static menu menu_pane;
	static mypage mypage_pane = new mypage();
	static admin admin_pane = new admin();
	
	
	
	public Main() {
		// 기본 창 크기 및 설정
		setTitle("내향적 책읽기");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1000, 700);
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
			menu_pane.addComponentListener(new menuClose());
			c.add(menu_pane);
		}
	}
	
	public class menuClose extends ComponentAdapter{
		public void componentHidden(ComponentEvent e) {
			if(menu_pane.selected == 0) {
				
			} else if(menu_pane.selected == 1) {
				
			} else if(menu_pane.selected == 2) {
				mypage_pane.addComponentListener(new ComponentAdapter() {
					public void componentHidden(ComponentEvent e) {
						Component com = e.getComponent();
						c.remove(com);
						c.repaint();
					}
				});
				c.add(mypage_pane);
			} else if(menu_pane.selected == 3){
				admin_pane.addComponentListener(new ComponentAdapter() {
					public void componentHidden(ComponentEvent e) {
						Component com = e.getComponent();
						c.remove(com);
						c.repaint();
					}
				});
				c.add(admin_pane);
			} else {
				
			}
		}
	}
	
	public static void main(String args[]) {
		new Main();
	}
}
