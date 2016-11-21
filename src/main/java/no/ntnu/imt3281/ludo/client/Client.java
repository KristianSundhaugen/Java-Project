package no.ntnu.imt3281.ludo.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import no.ntnu.imt3281.ludo.server.Message;



/**
 * 
 * This is the main class for the client. 
 * **Note, change this to extend other classes if desired.**
 * 
 * @author 
 *
 */
public class Client extends Application {
	
	@Override
	public void start(Stage primaryStage) {
		try {
			AnchorPane root = (AnchorPane)FXMLLoader.load(getClass().getResource("../gui/Ludo.fxml"));
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Called by the application when the JavaFX application is stopped
	 * Telling the connection to stop listening from requests from the server
	 */
	@Override
	public void stop() {
		Connection.stopConnection();
	}

	public static void main(String[] args) {
		launch(args);
	/**	 String serverAddress = "localhost";
		        Socket socket = new Socket(serverAddress, 9090);
		        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
		        out.println(new Message("chat message 1","CHAT", "1").toString());
		        out.println(new Message("game message 1","GAME", "4").toString());
		        out.println(new Message("chat message 2","CHAT", "2").toString());
		        out.println(new Message("game message 2","GAME", "4").toString());
		        out.println(new Message("chat message 3","CHAT", "1").toString());
		        out.println(new Message("game message 3","GAME", "4").toString());
		        out.println(new Message("chat message 4","CHAT", "4").toString());
		        out.println(new Message("game message 4","GAME", "4").toString());
	
		        BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		        String answer = input.readLine();
		        JOptionPane.showMessageDialog(null, answer);
		        System.exit(0);*/
	}
}
