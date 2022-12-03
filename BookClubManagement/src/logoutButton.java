import javax.swing.*;

public class logoutButton extends JButton{
	public logoutButton() {
		setBounds(10, 600, 30, 30);
		setText("로그아웃");
//		addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				session.login_member = null;
//				session.stored_page = -1;
//				
//				Main.c.add(Main.login_pane);
//				Main.login_pane.setVisible(false);
//				Main.login_pane.setVisible(true);
//			}
//		});
	}
}
