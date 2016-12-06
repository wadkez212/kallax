package Frame;

import javax.swing.JFrame;

import Connect.Const;
import kallax.ImagePanelServer;

public class FrameServer extends JFrame{
	public FrameServer(){
		//������� ������� ���� � �������
		JFrame frame = new JFrame("Server");				
	
		ImagePanelServer panel = new ImagePanelServer(Const.buttons);								//���������������� ����
		frame.setSize(400, 300);
		frame.add(panel);
		frame.setDefaultCloseOperation( EXIT_ON_CLOSE );
		frame.setVisible(true);
	}
}
