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
import no.ntnu.imt3281.ludo.gui.GameBoardController;



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

	public static void main(String[] args) throws UnknownHostException, IOException {
		launch(args);
//		 String serverAddress = "localhost";
//		        Socket socket = new Socket(serverAddress, 9090);
//		        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
//		        out.println("test1");
//		        System.out.println("1");
//		        out.println("test2");
//		        System.out.println("2");
//		        out.println("test3");
//		        System.out.println("3");
//		        out.println("test4");
//		        System.out.println("4");
//		        out.println("test5");
//		        System.out.println("5");
//		        out.println("test6");
//		        System.out.println("6");
		        //BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		        //String answer = input.readLine();
		        //JOptionPane.showMessageDialog(null, answer);
		        //System.exit(0);
	}
}
