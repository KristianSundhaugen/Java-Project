package no.ntnu.imt3281.ludo.client;

import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;



/**
 * 
 * This is the main class for the client. 
 * **Note, change this to extend other classes if desired.**
 * 
 * @author 
 *
 */
public class Client extends Application {
    private static Logger logger = Logger.getLogger("LogTest");

	@Override
	public void start(Stage primaryStage) {
		try {
			AnchorPane root = (AnchorPane)FXMLLoader.load(getClass().getResource("../gui/Ludo.fxml"));
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) { 
			logger.log(Level.INFO,"Missing Resource", e);
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
	
	/**
	 * Main program that is starting the client
	 * @param args the arguments used to start the program
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
