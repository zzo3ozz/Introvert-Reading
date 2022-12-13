package UI;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

public class SmallButton extends JButton {
	public SmallButton(String s) {
		super(s);
		setFont(Fonts.setFont(14, 1)); setBackground(Colors.mid); setForeground(Colors.base); setBorder(null);
		addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				setBackground(Colors.top);
			}
			
			public void mouseExited(MouseEvent e) {
				setBackground(Colors.mid);
			}
		});
	}
}
