package Tests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import Connect.Const;
import kallax.ImagePanelClient;
import kallax.ImagePanelServer;
import movePlayers.MovePlayer1Server;
import movePlayers.MovePlayer2Client;

public class TestServer {
	private ImagePanelServer Ps;
	private MovePlayer1Server mS;
	private ImagePanelClient Pc;
	private MovePlayer2Client mC;
	
	@Before
	public void Init(){
		Ps = new ImagePanelServer(Const.buttons);
		mS = new MovePlayer1Server();
		//comments
	}

	@Test
	public void testMoveS(){
		mS.moveS(0,1);

		if(!Const.buttons[0].getText().equals("0"))
			Assert.fail();
		
		for(int i = 1; i < 7; i++){
			if(!Const.buttons[i].getText().equals("7"))
				Assert.fail();
		}

	}
	
	@Test
	public void testDopCondition(){
		Const.buttons[5].setText("13");
		mS.moveS(5,1);
		mS.dopCondition(5);
		
		if(!Const.buttons[6].getText().equals("15"))
			Assert.fail();
	}
}
