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
	}
}
