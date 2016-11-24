package no.ntnu.imt3281.ludo.gui;


import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import no.ntnu.imt3281.ludo.client.Connection;
import no.ntnu.imt3281.ludo.server.UserMessage;
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
	
	private LudoController ludoController;
	private Tab tab;
	
	public PlayerLogin(){
		
	}
	
	private void playerLoggedIn(){
		Connection.newLoginRequest(this, username.getText(), password.getPassword().toString());
	}
	
	/**
	 * Sends a message to the server where the username and password is checked against the database
	 */
	@FXML
	public void handleLoginButton() {

		Connection.sendMessage(username.getText() + ":" + password.getPassword().toString(), "LOGIN_REQUEST", "-1");
	}
	/**
	 * Sends a message to the server where the username is checked againts the database
	 */
	@FXML 
	public void handleRegisterButton(){
		
		Connection.sendMessage(username.getText(), "REGISTER_REQUEST", "-1");
	}
	
	public void setLudoController(LudoController ludoController, Tab tab) {
		this.ludoController = ludoController;
		this.tab = tab;
	}

	public void loginResponse(UserMessage userMessage) {
		if(userMessage.intPart(1) == 0){
			loginMessage.setText("Wrong username or password");
		} else {
			Connection.getConnection().loggedIn();
			ludoController.removeTab(tab);
		}
	}
	
	public void registerResponse(UserMessage userMessage){
		if(userMessage.intPart(1) == 0){
			loginMessage.setText("Invalid username");
		} else {
			
		}
	}
}
