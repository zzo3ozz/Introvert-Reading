package UI;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.border.LineBorder;

import function.session;

public class MenuButton extends JButton{
	public MenuButton(String s) {
		super(s);
		setPreferredSize(new Dimension(250, 40));
		setFont(Fonts.setFont(20));
		setBackground(null);
		setForeground(Colors.base); 
		setBorder(null);
		
		addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				setFont(Fonts.setFont(20, 1));
				setBorder(new LineBorder(Colors.base, 2, true));
			}
			
			public void mouseExited(MouseEvent e) {
				setFont(Fonts.setFont(20));
				setBorder(null);
			}
		});
	}
}
