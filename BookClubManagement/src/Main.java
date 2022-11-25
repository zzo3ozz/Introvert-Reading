import javax.swing.*;

public class Main extends JFrame {
	public Main() {
		// 기본 창 크기 및 설정
		setTitle("내향적 책읽기");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1000, 700);
		setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	
	public static void main(String args[]) {
		new Main();
	}
}
