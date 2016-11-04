package no.ntnu.imt3281.ludo.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

/**
 * 
 * This is the main class for the server. 
 * **Note, change this to extend other classes if desired.**
 * 
 * @author 
 *
 */
public class Server  {
	private Vector<ServerClient> clients;
    private MessageReader receiver;
    public Server() throws IOException {
    	ServerSocket listener = new ServerSocket(9090);
    	System.out.println("new socket");
        while(true) {
        	Socket socket = listener.accept();
        	clients.add(new ServerClient(socket));
        	System.out.println("new connection");
        }
    	

        
        
        //MessageReceiver rec = new MessageReceiver(this,socket);
       // new Thread(rec).start();
       // System.out.println("new MessageReceiver");
    }
	public static void main(String[] args) {
        try {
			new Server();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
