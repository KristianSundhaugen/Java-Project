package no.ntnu.imt3281.ludo.gui;

import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
/**
 * 
 * @author Simen
 * This class will handle login
 * Check password and username
 */
public class PlayerLogin {
	
	@FXML
	private JButton loginButton;
	
	@FXML
	private JTextField username;
	
	@FXML
	private JPasswordField password;
	
	@FXML
	private JLabel loginMessage;
	
	public PlayerLogin(){
		
	}
	
	/**
	 * This will load the login scene and if both username and password is filled out
	 * the user will be taken to the GameBoard scene and can start a new game.
	 * @param event
	 */
	@FXML
	public void handleLoginButton(ActionEvent event) {
		
		//change to check for username and password in database
		if(username.getText().equals("") && password.getPassword().equals("")){
			try {
				//((Node)(event.getSource())).getScene().getWindow().hide(); //used to hide login screen
				Parent parent = FXMLLoader.load(getClass().getResource("GameBoard.fxml"));
				Stage stage = new Stage();
				Scene scene = new Scene(parent);
				stage.setScene(scene);
				stage.show();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else {
			loginMessage.setText("Username or password is invalid");			
		}
		
	}
	/**
	 * 
	 * @param event
	 */
	@FXML 
	public void handleRegisterButton(ActionEvent event){
		try {
			((Node)(event.getSource())).getScene().getWindow().hide();
			Parent parent = FXMLLoader.load(getClass().getResource("RegisterUser.fxml"));
			Stage registerStage = new Stage();
			Scene registerScene = new Scene(parent);
			registerStage.setScene(registerScene);
			registerStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
