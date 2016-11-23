package no.ntnu.imt3281.ludo.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerClient {
	private static final int PING_DELAY = 2000;
	private PrintWriter output;
	private BufferedReader input;
	private long lastMessageTime = System.currentTimeMillis() + PING_DELAY;
	private String status = "CONNECTED";
	private String username = "Player" + (int)(Math.random()*1000);
	public ServerClient(Socket socket) {
		try {
			this.output = new PrintWriter(socket.getOutputStream(), true);
			this.input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void sendMessage(String message) {
		System.out.println("Server sending: " + message);
		output.println(message);
	}
	public Message getMessage(){
		if (hasConnection()){
			try {
				if (!input.ready())
					return null;
				String msg = input.readLine();
				if (msg != null) {
					lastMessageTime = System.currentTimeMillis();
					return new Message(msg, this);
				}
			} catch (IOException e) {
				System.out.println("NOTHING READY");
			}
			return null;
		} else {
			return new Message("STATUS:" + status, this);
		}
	}

	public boolean hasConnection() {
		if (status == "DISCONNECTED")
			return false; 

		if (lastMessageTime < System.currentTimeMillis() - PING_DELAY){
			output.println("PING");
			this.lastMessageTime = System.currentTimeMillis();
		}
		   

		if (output.checkError())
			this.status = "DISCONNECTED";
			
		
		return !output.checkError();
	}
	public String getUsername() {
		return username;
	}
}
