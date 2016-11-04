package no.ntnu.imt3281.ludo.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerClient {
	private Socket socket;
	private PrintWriter output;
	private BufferedReader input;
	public ServerClient(Socket socket) {
		this.socket = socket;
		try {
			this.output = new PrintWriter(socket.getOutputStream(), true);
			this.input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void sendMessage(String message) {
		output.println(message);
	}
	public String getMessage(){
		try {
			return input.readLine();
		} catch (IOException e) {
			return null;
		}
	}
}
