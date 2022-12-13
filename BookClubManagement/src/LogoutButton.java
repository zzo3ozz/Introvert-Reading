import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.LineBorder;

import UI.*;

public class LogoutButton extends JButton{
	public LogoutButton() {
		setText("로그아웃");
		setBounds(108, 610, 84, 30);
		setFont(Fonts.setFont(16));
		setBackground(null);
		setForeground(Colors.base);
		setBorder(null);
		setHorizontalAlignment(JLabel.CENTER);
		addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Main.layer.removeAll();
				Main.layer.add(Main.login_pane, 0);
				Main.layer.add(Main.bg, 0);
				Main.login_pane.setVisible(true);
				Main.layer.repaint();
			}});
		
		addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				setText("<html><p style=\"text-decoration: underline; font-family: 'Gabia Maeumgyeol'; font-weight: bold\">"
						+ "로그아웃"
						+ "</p></html>");
			}
			
			public void mouseExited(MouseEvent e) {
				setText("로그아웃");
			}
		});
	}
}
