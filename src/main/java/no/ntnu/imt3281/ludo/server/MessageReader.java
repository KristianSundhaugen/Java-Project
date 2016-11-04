package no.ntnu.imt3281.ludo.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class MessageReader implements Runnable {
    private Server server;
	private Socket socket;
	
    public MessageReader(Server server, Socket socket) {
		this.server = server;
		this.socket = socket;
	}
	
	public void run() {
    	BufferedReader in;
		try {
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			System.out.println("Hello from a thread!");
			while(true){
				String line = in.readLine();
				System.out.println(line);            
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
		
	
	
}
