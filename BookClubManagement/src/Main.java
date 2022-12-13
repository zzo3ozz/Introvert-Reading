import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import function.*;
import struct.*;

public class Main extends JFrame {
	public static Container c = null;
	public static JLayeredPane layer = new JLayeredPane();
	public static background bg = new background();
	public static login_panel login_pane = new login_panel();
	public static menu menu_pane = null;
	public static rotation_panel rotation_pane = null;
	public static book_list book_pane = null;
	public static mypage mypage_pane = null;
	public static admin admin_pane = null;
	
	public Main() {
		// 기본 창 크기 및 설정
		setTitle("도서 관리 프로그램");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getRootPane().setPreferredSize(new Dimension(1000, 650));
		pack();
		setResizable(false);
		setLocationRelativeTo(null);
		
		c = getContentPane();
		c.setLayout(null);
		layer.setBounds(0, 0, 1000, 650);
		c.add(layer);
		
		layer.add(bg, 0);
		
		login_pane.addComponentListener(new loginClose());
		layer.add(login_pane, 0);
		
		setVisible(true);
	}
	
	public static class background extends JLabel {
		public background() {
			setPreferredSize(new Dimension(700, 650));
			setBounds(0, 0, 700, 650);
			ImageIcon icon = new ImageIcon("images/background.jpg");
			Image img = icon.getImage();
			img = img.getScaledInstance(700, 650, Image.SCALE_SMOOTH);
			icon = new ImageIcon(img);
			
			setIcon(icon);
		}
	}
	
	public class loginClose extends ComponentAdapter{
		public void componentHidden(ComponentEvent e) {
			Component com = e.getComponent();
			layer.remove(com);
			menu_pane = new menu();
			menu_pane.addComponentListener(new menuHidden());
			layer.add(menu_pane, 0);
		}
	}
	
	public class menuHidden extends ComponentAdapter{
		public void componentHidden(ComponentEvent e) {
			if(menu_pane.selected == 0) {
				rotation_pane = new rotation_panel();
				layer.add(rotation_pane, 1);
			} else if(menu_pane.selected == 1) {
				book_pane = new book_list();
				layer.add(book_pane, 1);
			} else if(menu_pane.selected == 2) {
				mypage_pane = new mypage();
				mypage_pane.addComponentListener(new hide_submenu());
				layer.add(mypage_pane, 1);
			} else if(menu_pane.selected == 3){
				admin_pane = new admin();
				admin_pane.addComponentListener(new hide_submenu());
				layer.add(admin_pane, 1);
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
