import java.awt.*;
import javax.swing.*;

public class LoginWin {
	public JPanel loginPane = new JPanel();
	
	public LoginWin() {
		loginPane.setBackground(Color.ORANGE);
		loginPane.setBounds(700, 0, 300, 700);
		loginPane.setLayout(null);
		
		JLabel idLa = new JLabel("ID"); idLa.setBounds(10, 250, 30, 20);
		JTextField idField = new JTextField(); idField.setBounds(50, 250, 150, 20);
		JLabel pwLa = new JLabel("PW"); pwLa.setBounds(10, 280, 30, 20);
		JPasswordField pwField = new JPasswordField(); pwField.setBounds(50, 280, 150, 20);

		loginPane.add(idLa); loginPane.add(pwLa);
		loginPane.add(idField); loginPane.add(pwField);
	}
}
