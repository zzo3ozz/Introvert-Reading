import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.LineBorder;

import function.*;
import struct.member;
import UI.*;

public class login_panel extends JPanel{
	private JTextField idField;
	private JPasswordField pwField;
	private JLabel error;
	
	public login_panel() {
		setBackground(Colors.mid);
		setBounds(700, 0, 300, 700);
		setLayout(null);

		JLabel idLa = new JLabel("ID");
		idLa.setBounds(30, 260, 35, 20); idLa.setFont(Fonts.setFont(15, 1)); idLa.setForeground(Colors.base);
		idField = new JTextField();
		idField.setFont(Fonts.setFont(12)); idField.setBorder(null);
		idField.setBounds(70, 260, 200, 20);
		
		JLabel pwLa = new JLabel("PW");
		pwLa.setBounds(30, 290, 35, 20); pwLa.setFont(Fonts.setFont(15, 1)); pwLa.setForeground(Colors.base);
		pwField = new JPasswordField();
		pwField.setBorder(null);
		pwField.setBounds(70, 290, 200, 20);
		
		LoginBtn btn = new LoginBtn();
		btn.setBounds(70, 340, 200, 40);
		error = new JLabel(); error.setBounds(70, 315, 200, 20);
		error.setFont(Fonts.setFont(12));
		error.setForeground(Color.RED);
		
		add(idLa);
		add(pwLa);
		add(idField);
		add(pwField);
		add(btn);
		add(error);
	}

	public class LoginBtn extends JButton{
		public LoginBtn() {
			super("로그인");
			setFont(Fonts.setFont(18));
			setForeground(Colors.base);
			setBackground(null);
			setBorder(new LineBorder(Colors.base, 2, true));
			addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String id = idField.getText();
					String pw = String.valueOf(pwField.getPassword());
					
					if(id.equals("")) {
						idField.requestFocus();
						error.setText("아이디를 입력하세요.");
						return ;
					}
					
					if(pw.equals("")) {
						pwField.requestFocus();
						error.setText("비밀번호를 입력하세요.");
						return ;
					}
			
					int state = login.tryLogin(id, pw);
					
					if(state == login.SUCCESS) {
						Main.login_pane.setVisible(false);
						session.now_team = rotation_f.getNowTeam(session.login_member.getNum());
						session.now_reading = rotation_f.getNowReading(session.now_team.get_r(), session.login_member.getNum());
					} else if(state == login.IDERROR) {
						idField.requestFocus();
						error.setText("등록된 아이디가 없습니다.");
					} else if(state == login.PWERROR) {
						pwField.requestFocus();
						error.setText("비밀번호가 일치하지 않습니다.");
					}
				}
			});
			
			addMouseListener(new MouseAdapter() {
				public void mouseEntered(MouseEvent e) {
					setFont(Fonts.setFont(18, 1));
					setBorder(new LineBorder(Colors.top, 2, true));
					setBackground(Colors.base);
					setForeground(Colors.top);
				}
				
				public void mouseExited(MouseEvent e) {
					setFont(Fonts.setFont(18));
					setBackground(null);
					setBorder(new LineBorder(Colors.base, 2, true));
					setForeground(Colors.base);
				}
			});
		}
		
	}
}
