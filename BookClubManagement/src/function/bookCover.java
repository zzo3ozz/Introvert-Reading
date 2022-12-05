package function;

import java.awt.Image;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class bookCover {
	public static ImageIcon getIcon() {
		ImageIcon icon = new ImageIcon("images/default.png");
		Image temp_img = icon.getImage();
		temp_img = temp_img.getScaledInstance(140, 200, Image.SCALE_SMOOTH);
		icon = new ImageIcon(temp_img);
		
		return icon;
	}
	
	public static ImageIcon getIcon(String path) {
		ImageIcon icon = new ImageIcon("images/default.png");
		Image temp_img = icon.getImage();
		
		try {
			URL url = new URL(path);
			temp_img = ImageIO.read(url);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		temp_img = temp_img.getScaledInstance(140, 200, Image.SCALE_SMOOTH);
		icon = new ImageIcon(temp_img);
		
		return icon;
	}
}
