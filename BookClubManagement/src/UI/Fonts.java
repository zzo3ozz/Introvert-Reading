package UI;

import java.awt.Font;

public class Fonts {
	public static Font setFont(int size) {
		Font style = new Font("Gabia Maeumgyeol", Font.PLAIN, size);
		return style;
	}
	
	public static Font setFont(int size, int bold) {
		Font style = new Font("Gabia Maeumgyeol", Font.BOLD, size);
		return style;
	}
}
