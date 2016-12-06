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
	eHandlerServer handlerServer = new eHandlerServer();		//������� ��������� ��� ������ �������
	
	public MovePlayer1Server()
	{	    
	    //������� ������ ������ ������� ��������
		if(endGame == false){
			for(int i = 0; i < 6; i++)										
				Const.buttons[i].addActionListener(handlerServer);
		}

        //��� ������� �� ������ ���������� � ���������
        
		try{
			ss = new ServerSocket(Const.port);
			s = ss.accept();
			System.out.println("Client connected");
			in = new BufferedReader(new InputStreamReader(s.getInputStream()));		//������� �� ������
			out = new PrintWriter(s.getOutputStream(), true);						//���-�� ������� � �����
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
				System.out.println("Server" + ": " + str);		//������� ��������� �� �������
				
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
	                str = in.readLine(); 					//��������� ��������� ���������� �� �������(���� �� ����� � �������, �� ��� ��������� �������(������ ����� ���� ������) ��� ������ ��� ������ � ������ exeption)

	        		synchronized(Const.class)
	        		{
	        			Const.cell++;
	        			Const.cell %= 14;
	        		}
	        		Const.buttons[Const.cell].setText(str);	//���������� ������ � ������� ����� ����  �������

				    ImageIcon img = new ImageIcon("strelkaVniz.jpg");					
				    JLabel l1 = new JLabel();
				     JButton strelka = new JButton();
				     ImageIcon icon = new ImageIcon(img.getImage().getScaledInstance(25, 18, img.getImage().SCALE_DEFAULT));
				     Const.buttons[14].setIcon(icon);
				     
					 //��� ������ ������� ������ ��� ������� � ���� �� ��������� �� ��� ���
				     //�� �� ������� ������ � ����� ����� ��������
				     for(int i = 0; i < 6; i++)										
				       	Const.buttons[i].removeActionListener(handlerServer);
					 //������� ������ ������ ������� �������� �� �� ����� �� �� ���������
				     for(int i = 0; i < 6; i++)										
				    	 Const.buttons[i].addActionListener(handlerServer);
	        		
	        		
					if(str == null)							//������ ������
					{
						System.out.println("Client disconnected.");
						closeAll();
						break;
					}
					
					System.out.println("Client: " + str);		//������� ��������� �� �������
					
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
		int chislo1 = Integer.parseInt(Const.buttons[i].getText());	//�������� ����� � ������� ������
		while(chislo1 != 0)
		{
			chislo1--;
			if((i+last)%14 == 13) last++;											//����� ���������� �� ����������
			int chislo2 = Integer.parseInt(Const.buttons[(i+last)%14].getText());	//�������� ����� � ������ ��������� ������
			chislo2++;																//����������� ��� �� 1
			if((i + last)%14  == i) krug++;											//���� ��������� ����� ����� ��� � ������� ������ ������ �� ������ ����
			Const.buttons[(i+last)%14].setText("" + chislo2);						//����������� ������ ��� �����
			last++;
		}
		
		Const.buttons[i].setText("" + krug);										//���������� ������� ������
		this.last = last;
	}
	
	public void dopCondition(int i){
		//���� � ����� ���� ����� ������� ���� � ���� ��� � ������ �����
		if((i + last - 1)%14 < 6 && (i + last - 1)%14 >= 0)
		{
			int chislo1 = Integer.parseInt(Const.buttons[(i + last - 1)%14].getText());		//�������� � ��������� �����
			if(chislo1 == 1)															//��������� ����� ���� ������, �� ����� ���� � ��� ��������� 1
			{		
				System.out.println((i + last - 1)%14);
				chislo1--;																//��������� ��
				Const.buttons[(i + last - 1)%14].setText("" + chislo1);					//����������� �� ��� ��������
				int chisloInKallax = Integer.parseInt(Const.buttons[6].getText());		//�������� ����� � ������
				chisloInKallax++;														//����������� ��� �� 1
				int lunkaNaprotiv = Integer.parseInt(Const.buttons[12 - (i + last - 1)%14].getText());		//�������� ����� � ����� ��������
				chisloInKallax += lunkaNaprotiv;										//���������� � ������ ��� ���� � ����� ��������
				Const.buttons[6].setText("" + chisloInKallax);							//����������� ������ ��� ��������
				lunkaNaprotiv = 0;														//����� �������� ���������� ������ 0(�� ��� ��������)
				Const.buttons[12 - (i + last - 1)%14].setText("" + lunkaNaprotiv);
			}
		}
		this.last = last;
	}
	
	public void endMove(int i){
		//���� ��������� ���� ����� �� � ��� ������, �� ������ ������ �� ����
		//� ��������� ������ ����� ��� ���
		//���� ����� �� � ��� ������, �� ������� ��� ������
		//������ ������ ������
		if((i + last - 1)%14 != 6)
		{
	        ImageIcon img = new ImageIcon("strelkaVverh.jpg");					//���������� ������� �����
	        JLabel l1 = new JLabel();
	        JButton strelka = new JButton();
	        ImageIcon icon = new ImageIcon(img.getImage().getScaledInstance(25, 18, img.getImage().SCALE_DEFAULT));
	        Const.buttons[14].setIcon(icon);
	        
			for(int j = 0; j < 6; j++)
			{
				Const.buttons[j].removeActionListener(handlerServer);
				last = 1;
			}
			
	        //�������� ������ �� �������
	        String str = "";
			for(int j = 0; j < 14; j++)
			{
				str = Const.buttons[j].getText();	//�������� ���� � ������� ������
				out.println(str);					//�������� ��
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
	
	//�������������� � ��������
	public class eHandlerServer implements ActionListener
	{	
		public void actionPerformed(ActionEvent e) {
			for(int i = 0; i < 6; i++)
			{
				if(e.getSource() == Const.buttons[i])						//���� �������� �� ������
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
