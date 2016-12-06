package movePlayers;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import Connect.Const;


public class MovePlayer1Server extends JFrame {
	private ServerSocket ss;
	private Socket s;
	private BufferedReader in;
	private PrintWriter out;
	private BufferedReader stdin;
	private boolean endGame = false;
	eHandlerServer handlerServer = new eHandlerServer();		//создаем слушатель для кнопок сервера
	
	public MovePlayer1Server()
	{	    
	    //придали каждой кнопке сервера действие
		if(endGame == false){
			for(int i = 0; i < 6; i++)										
				Const.buttons[i].addActionListener(handlerServer);
		}

        //при нажатии на кнопку обращаемся к слушателю
        
		try{
			ss = new ServerSocket(Const.port);
			s = ss.accept();
			System.out.println("Client connected");
			in = new BufferedReader(new InputStreamReader(s.getInputStream()));		//считать из сокета
			out = new PrintWriter(s.getOutputStream(), true);						//что-то послать в сокет
			stdin = new BufferedReader(new InputStreamReader(System.in));	
			
			Resender resend = new Resender();
			resend.start();
			
			while(true)
			{
				/*str = stdin.readLine();
				if(str.equals("@quit"))
				{
					out.println(str);
					break;
				}
				System.out.println("Server" + ": " + str);		//выводим сообщение от сервера
				
				out.println(str);*/
			}
			
		}catch(Exception e){	
			//e.printStackTrace();
			System.out.print("Server disconnected!");
		} finally {
			closeAll();
		}
	}
	
	private void closeAll() {
		try {
			ss.close();
			in.close();
			out.close();
			s.close();
			stdin.close();
		} catch (Exception e) {
			System.err.println("Error.");
		}
	}
		
	private class Resender extends Thread { 
		String str = "";
	    
	    @Override 
	    public void run() { 
	    	String filename = "";
	    	
	        try { 
	            while(true){
	                str = in.readLine(); 					//считывает сообщение полученное от сервера(если мы вышли с сервера, то при последнем запуске(тобишь когда тоже выйдем) это строка уже пустая и выдаст exeption)

	        		synchronized(Const.class)
	        		{
	        			Const.cell++;
	        			Const.cell %= 14;
	        		}
	        		Const.buttons[Const.cell].setText(str);	//наращиваем калахи у сервера после хода  клиента

				    ImageIcon img = new ImageIcon("strelkaVniz.jpg");					
				    JLabel l1 = new JLabel();
				     JButton strelka = new JButton();
				     ImageIcon icon = new ImageIcon(img.getImage().getScaledInstance(25, 18, img.getImage().SCALE_DEFAULT));
				     Const.buttons[14].setIcon(icon);
				     
					 //при первом проходе кнопки уже созданы и дабы не создавать их еще раз
				     //мы их сначала удалим а потом снова создадим
				     for(int i = 0; i < 6; i++)										
				       	Const.buttons[i].removeActionListener(handlerServer);
					 //придали каждой кнопке сервера действие тк до этого мы их отключали
				     for(int i = 0; i < 6; i++)										
				    	 Const.buttons[i].addActionListener(handlerServer);
	        		
	        		
					if(str == null)							//клиент падает
					{
						System.out.println("Client disconnected.");
						closeAll();
						break;
					}
					
					System.out.println("Client: " + str);		//выводим сообщение от клиента
					
					endGame();
	            } 
	        } catch (IOException e) { 
	            System.out.println("Can't get message. Client shutdown.");
	        } finally {
				closeAll();
			}
	    } 
	} 
	
	int last;
	public void moveS(int i, int last){
		int krug = 0;
		int chislo1 = Integer.parseInt(Const.buttons[i].getText());	//получаем число в текущей кнопке
		while(chislo1 != 0)
		{
			chislo1--;
			if((i+last)%14 == 13) last++;											//лунку противника не наращиваем
			int chislo2 = Integer.parseInt(Const.buttons[(i+last)%14].getText());	//получаем число в каждой следующей кнопке
			chislo2++;																//увеличиваем его на 1
			if((i + last)%14  == i) krug++;											//если следующая лунка равно той с которой ходили значит мы прошли круг
			Const.buttons[(i+last)%14].setText("" + chislo2);						//присваиваем кнопке это число
			last++;
		}
		
		Const.buttons[i].setText("" + krug);										//наращиваем текущую кнопку
		this.last = last;
	}
	
	public void dopCondition(int i){
		//если в конце хода игрок положил очко в свой ряд в пустую лунку
		if((i + last - 1)%14 < 6 && (i + last - 1)%14 >= 0)
		{
			int chislo1 = Integer.parseInt(Const.buttons[(i + last - 1)%14].getText());		//значение в последней лунке
			if(chislo1 == 1)															//последняя лунка была пустая, но после хода в ней оказалось 1
			{		
				System.out.println((i + last - 1)%14);
				chislo1--;																//уменьшаем ее
				Const.buttons[(i + last - 1)%14].setText("" + chislo1);					//присваиваем ей это значение
				int chisloInKallax = Integer.parseInt(Const.buttons[6].getText());		//получаем число в калахе
				chisloInKallax++;														//увеличиваем его на 1
				int lunkaNaprotiv = Integer.parseInt(Const.buttons[12 - (i + last - 1)%14].getText());		//получаем число в лунке напротив
				chisloInKallax += lunkaNaprotiv;										//прибавляем в каллах все очки с лунки напротив
				Const.buttons[6].setText("" + chisloInKallax);							//присваиваем калаху это значенме
				lunkaNaprotiv = 0;														//лунка напротив становится равной 0(тк все извлекли)
				Const.buttons[12 - (i + last - 1)%14].setText("" + lunkaNaprotiv);
			}
		}
		this.last = last;
	}
	
	public void endMove(int i){
		//если последнее очко легло не в мой каллах, то больше ходить не могу
		//в противном случае делаю еще ход
		//если легло не в мой каллах, то отрубаю все кнопки
		//должен ходить клиент
		if((i + last - 1)%14 != 6)
		{
	        ImageIcon img = new ImageIcon("strelkaVverh.jpg");					//установили стрелку вверх
	        JLabel l1 = new JLabel();
	        JButton strelka = new JButton();
	        ImageIcon icon = new ImageIcon(img.getImage().getScaledInstance(25, 18, img.getImage().SCALE_DEFAULT));
	        Const.buttons[14].setIcon(icon);
	        
			for(int j = 0; j < 6; j++)
			{
				Const.buttons[j].removeActionListener(handlerServer);
				last = 1;
			}
			
	        //передали данные на клиента
	        String str = "";
			for(int j = 0; j < 14; j++)
			{
				str = Const.buttons[j].getText();	//получаем очки в нулевой кнопке
				out.println(str);					//передаем их
			}
		}
	}
	
	public void endGame(){
		
		int pointInKallaxServer = Integer.parseInt(Const.buttons[6].getText());
		int pointInKallaxClient = Integer.parseInt(Const.buttons[13].getText());
		
		if(pointInKallaxServer >=36 && endGame == false)
		{
			endGame = true;
			JFrame theWinner = new JFrame("You Win!");
			theWinner.add(new JButton("You Win!"));
			theWinner.setSize(400, 300);
			theWinner.setDefaultCloseOperation( EXIT_ON_CLOSE );
			theWinner.setVisible(true);
			
			for(int j = 0; j < 7; j++)
			{
				Const.buttons[j].removeActionListener(handlerServer);
				last = 1;
			}
		}
		
		if(pointInKallaxClient >= 36 && endGame == false)
		{
			endGame = true;
			JFrame theLose = new JFrame("You Lose!");
			theLose.add(new JButton("You Lose!"));
			theLose.add(new JButton("You Win!"));
			theLose.setSize(400, 300);
			theLose.setDefaultCloseOperation( EXIT_ON_CLOSE );
			theLose.setVisible(true);
			
			for(int j = 0; j < 7; j++)
			{
				Const.buttons[j].removeActionListener(handlerServer);
				last = 1;
			}
		}
	}
	
	//взаимодействие с кнопками
	public class eHandlerServer implements ActionListener
	{	
		public void actionPerformed(ActionEvent e) {
			for(int i = 0; i < 6; i++)
			{
				if(e.getSource() == Const.buttons[i])						//если щелкнули на кнопку
				{
					moveS(i, last);
					dopCondition(i);
					endMove(i);
					endGame();
				}
			}
			
		}
	}
}
