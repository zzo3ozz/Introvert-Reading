import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class LogoutButton extends JButton{
	public LogoutButton() {
		setPreferredSize(new Dimension(40, 40));
		setText("로그아웃");
		addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Main.c.removeAll();
				Main.c.add(Main.login_pane);
				Main.login_pane.setVisible(true);
				Main.c.repaint();
			}});
	}
}
