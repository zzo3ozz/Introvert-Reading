import java.awt.*;
import javax.swing.*;

public class navigation extends JPanel{
	private ImageIcon icon = new ImageIcon("images/background.jpg");
	private Image img = icon.getImage().getScaledInstance(700, 650, Image.SCALE_SMOOTH);
	public navigation() {
		setBounds(0, 0, 70, 650);
		setBackground(new Color(0, 0, 0, 0));
		setLayout(null);
		
		BackButton back_btn = new BackButton();
		back_btn.setBounds(15, 15, 40, 40);
		add(back_btn);
	}
	
}