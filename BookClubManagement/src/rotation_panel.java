import java.awt.*;
import javax.swing.*;

import function.rotation_f;
import function.session;

public class rotation_panel extends JPanel {
	public rotation_panel() {
		System.out.println("rotation_pane");
		setBounds(0, 0, 1000, 650);
		setLayout(null);
		
		session.now_team = rotation_f.getNowTeam(session.login_member.getNum());

		add(new navigation());
		
		JPanel content = new JPanel();
					
		content.setBackground(Color.ORANGE);
		content.setBounds(70, 0, 930, 650);
		content.setPreferredSize(new Dimension(930, 650));
		content.setLayout(null);
		
		JPanel now_reading = new JPanel();
		now_reading.setBounds(420, 40, 470, 570);
		now_reading.setBackground(Color.gray);
		
		content.add(new now_rotation());
		content.add(now_reading);
		add(content);
		
		setVisible(true);
	}
	
	public class now_rotation extends JPanel {
		public now_rotation() {
			setBounds(40, 40, 350, 570);
			setLayout(new FlowLayout());
			
			JLabel title = new JLabel("현재 로테이션 정보");
			//title.setBounds(10, 10, 330, 40);
			
			JButton btn = new JButton("등록 / 수정");
			
			JLabel r_num = new JLabel("회차 : " + Integer.toString(session.now_team.get_r()));
			JLabel date = new JLabel("기간 : " + session.now_team.get_start().toString() + " ~ " + session.now_team.get_end().toString());
			JLabel members = new JLabel("멤버 : " + session.now_team.getMembersByString());
			JLabel books = new JLabel("책 : " + session.now_team.getBookByString());
			
			add(title);
			
			add(btn);
			add(r_num); add(date); add(members); add(books);
		}
		
	}
}
