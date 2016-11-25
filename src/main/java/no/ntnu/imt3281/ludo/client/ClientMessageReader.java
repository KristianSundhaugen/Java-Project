package no.ntnu.imt3281.ludo.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.invoke.MethodHandles;
import java.net.Socket;
import java.util.logging.Logger;

import javafx.application.Platform;
import no.ntnu.imt3281.ludo.server.Message;

public class ClientMessageReader implements Runnable {
	private static Logger logger = Logger.getLogger(Globals.LOG_NAME);
	private BufferedReader input;
	private boolean stop = false;
	
	/**
	 * Constructor creating a new BufferdReader
	 * @param socket the socket with the connection to the server
	 */
	public ClientMessageReader(Socket socket) {
		try {
			this.input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e) {
			logger.throwing(this.getClass().getName(), "ClientMessageReader", e);
		}
	}
	
	/**
	 * Trying to read messages from the socket all the time
	 */
	public void run() {
		while (!stop) {
			Message msg = getMessage();
			if (msg != null && !msg.isPing()) {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						Connection.messageParser(msg);
					}
				});
			}
			try {Thread.sleep(50);} catch (InterruptedException e) {
				logger.throwing(this.getClass().getName(), "run", e);
				Thread.currentThread().interrupt();
			}
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
		} catch (IOException e) {
			logger.throwing(this.getClass().getName(), "getMessage", e);
		}
		return null;
	}

	/**
	 * Stopping reading messages making the thread stop
	 */
	public void stop() {
		this.stop  = true;
	}	
}
