import java.awt.*;
import javax.swing.*;

public class navigation extends JPanel{
	public navigation() {
		setBounds(0, 0, 70, 700);
		setBackground(Color.WHITE);
		setLayout(null);
		
		BackButton back_btn = new BackButton();
		back_btn.setBounds(10, 10, 20, 20);
		add(back_btn);
	}
}