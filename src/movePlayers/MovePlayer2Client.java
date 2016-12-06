package movePlayers;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import Connect.Const;
import kallax.Main;

public class MovePlayer2Client extends JFrame {
	private Socket s;
	private BufferedReader in;
	private PrintWriter out;
	private BufferedReader stdin;
	
	private BufferedInputStream reader;
	private BufferedOutputStream writer;
	private boolean endGame = false;
	eHandlerClient handlerClient = new eHandlerClient();		//создаем слушатель для кнопок клиента
	
	public MovePlayer2Client()
	{   
      //Изначально ходить нельзя
        //for(int i = 7; i < 13; i++)										
        	//Const.buttons[i].addActionListener(handlerClient);
        
		try{
			s = new Socket(Const.ip, Const.port);
			in = new BufferedReader(new InputStreamReader(s.getInputStream()));	//считать из сокета
			out = new PrintWriter(s.getOutputStream(), true);						//что-то послать в сокет
			stdin = new BufferedReader(new InputStreamReader(System.in));		//вводим строку
			
			Resender resend = new Resender();
			resend.start();
			
			String str = "";
			while (true) 
			{
		
			}
		} catch(Exception e){	
			//e.printStackTrace();
			System.out.print("Server disconnected");
		} finally {
			close();
		}
	}
		
	private void close() {
		try {
			in.close();
			out.close();
			s.close();
			stdin.close();
		} catch (Exception e) {
			System.err.println("Compound disconnect.");
		}
	}
	
	//Но как нам печатать в консоль полученные от сервера сообщения? Ведь нам 
	//нужно одновременно ожидать сообщений из консоли (от пользователя) и сообщений 
	//из потока (от сервера). Придётся для этого создать дополнительную нить.
	//Создадим внутренний класс, который будет получать сообщения от сервера и выводить их в консоль.
	private class Resender extends Thread { 
		String str = "";
		
	    @Override 
	    public void run() { 
	   
	        try { 
	            while(true) { 
	                str = in.readLine(); 					//считывает сообщение полученное от сервера(если мы вышли с сервера, то при последнем запуске(тобишь когда тоже выйдем) это строка уже пустая и выдаст exeption)

	        		synchronized(Const.class)
	        		{
	        			Const.cell++;						//наращиваем калахи у клиента
	        			Const.cell %= 14;
	        		}
	        		Const.buttons[Const.cell].setText(str);	//наращиваем калахи у клиента после хода  сервера

				    ImageIcon img = new ImageIcon("strelkaVniz.jpg");					//установили стрелку вверх
				    JLabel l1 = new JLabel();
				    JButton strelka = new JButton();
				    ImageIcon icon = new ImageIcon(img.getImage().getScaledInstance(25, 18, img.getImage().SCALE_DEFAULT));
				    Const.buttons[14].setIcon(icon);
				        
				    //при первом проходе кнопки уже созданы и дабы не создавать их еще раз
				    //мы их сначала удалим а потом снова создадим
				    for(int i = 7; i < 13; i++)										
					     Const.buttons[i].removeActionListener(handlerClient);
				    //вернули клиенту воможность ходить
					if(endGame == false){
						for(int i = 7; i < 13; i++)										
							Const.buttons[i].addActionListener(handlerClient);
					}

	        		
					if(str == null)  					//если клиент отключился
					{
						System.out.println("Client exit chat.");
						close();
						break;
					}
	                System.out.println("Server" + ": " + str);		//выводим сообщение от сервера
	                
					endGame();
	            } 
	        } catch (IOException e) { 
	            System.out.println("Server shutdown.");
	            System.exit(0);
	        }
	    } 
	}
	
	int last = 1;
	public void moveC(int i, int last){
		int krug = 0;

	    int chislo1 = Integer.parseInt(Const.buttons[i].getText());	//получаем число в текущей кнопке
	    while(chislo1 != 0)
	    {
	    	chislo1--;
	       	if((i+last)%14 == 6) last++;											
	       	int chislo2 = Integer.parseInt(Const.buttons[(i+last)%14].getText());	
	       	chislo2++;																
	       	if((i + last)%14  == i) krug++;											
	       	Const.buttons[(i+last)%14].setText("" + chislo2);						
	       	last++;
	    }        
	    Const.buttons[i].setText("" + krug);
	    this.last = last;
	}
		
	public void dopCondition(int i){
		if((i + last - 1)%14 < 13 && (i + last - 1)%14 > 6)
		{
			int chislo1 = Integer.parseInt(Const.buttons[(i + last - 1)%14].getText());
			System.out.println(chislo1);
			if(chislo1 == 1)															//последняя лунка была пустая, но после хода в ней оказалось 1
			{		
				System.out.println((i + last - 1)%14);
				chislo1--;																//уменьшаем ее
				Const.buttons[(i + last - 1)%14].setText("" + chislo1);					//присваиваем ей это значение
				int chisloInKallax = Integer.parseInt(Const.buttons[13].getText());		//получаем число в калахе
				chisloInKallax++;														//увеличиваем его на 1
				int lunkaNaprotiv = Integer.parseInt(Const.buttons[12 - (i + last - 1)%14].getText());		//получаем число в лунке напротив
				chisloInKallax += lunkaNaprotiv;										//прибавляем в каллах все очки с лунки напротив
				Const.buttons[13].setText("" + chisloInKallax);							//присваиваем калаху это значенме
				lunkaNaprotiv = 0;														//лунка напротив становится равной 0(тк все извлекли)
				Const.buttons[12 - (i + last - 1)%14].setText("" + lunkaNaprotiv);
			}
		}
	}
	
	public void endMove(int i){
		//последнее очко упало не в каллах клиента, то завершаем ход и отрубаем кнопки
		if((i + last - 1)%14 != 13)
		{
	        ImageIcon img = new ImageIcon("strelkaVverh.jpg");					//установили стрелку вверх
	        JLabel l1 = new JLabel();
	        JButton strelka = new JButton();
	        ImageIcon icon = new ImageIcon(img.getImage().getScaledInstance(25, 18, img.getImage().SCALE_DEFAULT));
	        Const.buttons[14].setIcon(icon);
	        
			for(int j = 7; j < 13; j++)
			{
				Const.buttons[j].removeActionListener(handlerClient);
				last = 1;
			}
		}
        
        //передали данные на сервер
        String str = "";
		for(int j = 0; j < 14; j++)
		{
			str = Const.buttons[j].getText();	//получаем очки в нулевой кнопке
			out.println(str);					//передаем их
		}
	}
	
	public void endGame(){
		
		int pointInKallaxServer = Integer.parseInt(Const.buttons[6].getText());
		int pointInKallaxClient = Integer.parseInt(Const.buttons[13].getText());
		
		if(pointInKallaxServer >= 36 && endGame == false)
		{
			endGame = true;
			JFrame theLose = new JFrame("You Lose!");
			theLose.add(new JButton("You Lose!"));
			theLose.setSize(400, 300);
			theLose.setDefaultCloseOperation( EXIT_ON_CLOSE );
			theLose.setVisible(true);
			
			for(int j = 7; j < 13; j++)
			{
				Const.buttons[j].removeActionListener(handlerClient);
				last = 1;
			}
		}
		
		if(pointInKallaxClient >= 36 && endGame == false)
		{
			endGame = true;
			JFrame theWinner = new JFrame("You Win!");
			theWinner.add(new JButton("You Win!"));
			theWinner.setSize(400, 300);
			theWinner.setDefaultCloseOperation( EXIT_ON_CLOSE );
			theWinner.setVisible(true);
			
			for(int j = 7; j < 13; j++)
			{
				Const.buttons[j].removeActionListener(handlerClient);
				last = 1;
			}
		}
		
	}
	
	//взаимодействие с кнопками
	public class eHandlerClient implements ActionListener
	{	
		public void actionPerformed(ActionEvent e) {
			for(int i = 7; i < 13; i++)
			{
				if(e.getSource() == Const.buttons[i])						//если щелкнули на кнопку
				{
					moveC(i, last);
					dopCondition(i);
					endMove(i);     
					endGame();
				}
			}			
		}
	}
}

