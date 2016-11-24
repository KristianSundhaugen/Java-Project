package no.ntnu.imt3281.ludo.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import no.ntnu.imt3281.ludo.server.Message;

public class ClientMessageReader implements Runnable {
    private Connection connection;
	private BufferedReader input;
	private boolean stop = false;
	
    public ClientMessageReader(Connection connection, Socket socket) {
		this.connection = connection;
		try {
			this.input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e) {}

	}
	
	/**
	 * Trying to read messages from the socket all the time
	 */
	public void run() {
		while (!stop) {
			Message msg = getMessage();
			if (msg != null && !msg.isPing())
				connection.messageParser(msg);
			try {Thread.sleep(100);} catch (InterruptedException e) {}
		}    	
	}

	/**
	 * Trying to read a message from the input stream if there is new input available
	 * @return a message object
	 */
	private Message getMessage() {
		try {
			if (!input.ready())
				return null;
			String msg = input.readLine();
			if (msg != null)
				return new Message(msg);
		} catch (IOException e) {}
		return null;
	}

	public void stop() {
		this.stop  = true;
		
	}	
	
			
}
