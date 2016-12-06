package kallax;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import javax.swing.*;

import Connect.Const;
import Frame.FrameClient;
import Frame.FrameServer;
import movePlayers.MovePlayer1Server;
import movePlayers.MovePlayer2Client;


public class Main {
	
    public Main() {
    	
        Scanner in = new Scanner(System.in);
        System.out.println("You are gaming on Server or Client(S(erver) / C(lient))");
        while (true) {
			char answer = Character.toLowerCase(in.nextLine().charAt(0));
			if (answer == 's') {
				new FrameServer();
				new MovePlayer1Server();
				break;
			} else if (answer == 'c') {
				new FrameClient();
				new MovePlayer2Client();
				break;
			} else {
				System.out.println("Invalid input. Please repeat.");
			}
		}  
    }

    public static void main(String[] args) {
        new Main();
    }
}