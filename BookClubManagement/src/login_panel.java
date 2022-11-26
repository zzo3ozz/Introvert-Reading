import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import function.*;
import struct.member;

public class login_panel extends JPanel{
	private JTextField idField;
	private JPasswordField pwField;
	private JLabel error;
	
	public login_panel() {
		setBackground(Color.ORANGE);
		setBounds(700, 0, 300, 700);
		setLayout(null);

		JLabel idLa = new JLabel("ID");
		idLa.setBounds(10, 250, 30, 20);
		idField = new JTextField();
		idField.setBounds(50, 250, 140, 20);
		
		JLabel pwLa = new JLabel("PW");
		pwLa.setBounds(10, 280, 30, 20);
		pwField = new JPasswordField();
		pwField.setBounds(50, 280, 140, 20);
		
		JButton btn = new JButton("로그인");
		btn.setBounds(200, 250, 80, 30);
		error = new JLabel(); error.setBounds(50, 310, 250, 20);
		error.setForeground(Color.RED);
		
		btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String id = idField.getText();
				String pw = String.valueOf(pwField.getPassword());
				int state = login.tryLogin(id, pw);
				
				if(state == login.SUCCESS) {
					setVisible(false);
				} else if(state == login.IDERROR) {
					error.setText("등록된 아이디가 없습니다.");
				} else if(state == login.PWERROR) {
					error.setText("비밀번호가 일치하지 않습니다.");
				}
			}
		});

		add(idLa);
		add(pwLa);
		add(idField);
		add(pwField);
		add(btn);
		add(error);
	}
}
