package Frame;

import javax.swing.JFrame;

import Connect.Const;
import kallax.ImagePanelClient;

public class FrameClient extends JFrame {
	public FrameClient(){
		JFrame frame = new JFrame("Client");
		ImagePanelClient panel = new ImagePanelClient(Const.buttons);								//инициализировали поле
		frame.setSize(400, 300);
        frame.add(panel);
        frame.setDefaultCloseOperation( EXIT_ON_CLOSE );
        frame.setVisible(true);
	}
}
