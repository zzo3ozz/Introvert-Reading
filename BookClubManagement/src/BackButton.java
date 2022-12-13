import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import UI.Colors;
import function.session;

public class BackButton extends JButton {
	private ImageIcon icon = new ImageIcon((new ImageIcon("images/back.png")).getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH));
	private ImageIcon bicon = new ImageIcon((new ImageIcon("images/back_black.png")).getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH));
	public BackButton() {
		setPreferredSize(new Dimension(40, 40));
		setIcon(icon);
		
		setBorderPainted(false);
		setContentAreaFilled(false);
		setFocusPainted(false);
		setRolloverEnabled(false);
		
		setPressedIcon(bicon);
		
		addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Component com = ((BackButton)e.getSource()).getParent();
				
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
				
				Main.layer.remove(com);
				Main.layer.repaint();
			}
		});
//		addMouseListener(new MouseAdapter() {
//			public void mouseEntered(MouseEvent e) {
//				icon.setOpaque(false);
//				setIcon(bicon);
//			}
//			
//			public void mouseExited(MouseEvent e) {
//				setIcon(icon);
//			}
//		});
	}
}
