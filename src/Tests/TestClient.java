package Tests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import Connect.Const;
import kallax.ImagePanelClient;
import kallax.ImagePanelServer;
import movePlayers.MovePlayer1Server;
import movePlayers.MovePlayer2Client;

public class TestClient {
	private ImagePanelClient Pc;
	private MovePlayer2Client mC;
	
	@Before
	public void Init(){
		Pc = new ImagePanelClient(Const.buttons);
		mC = new MovePlayer2Client();
	}

	@Test
	public void testMoveS(){
		mC.moveC(7,1);

		if(!Const.buttons[7].getText().equals("0"))
			Assert.fail();
		
		for(int i = 8; i < 14; i++){
			if(!Const.buttons[i].getText().equals("7"))
				Assert.fail();
		}

	}
	
	@Test
	public void testDopCondition(){
		Const.buttons[7].setText("" + 13);
		mC.moveC(7,1);
		mC.dopCondition(7);
		
		if(!Const.buttons[13].getText().equals("15"))
			Assert.fail();
	}
}
