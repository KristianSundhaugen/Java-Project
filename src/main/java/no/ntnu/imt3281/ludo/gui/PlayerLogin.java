package no.ntnu.imt3281.ludo.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
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
	private Button loginButton;
	
	@FXML
	private TextField username;
	
	@FXML
	private PasswordField password;
	
	@FXML
	private Label loginMessage;
	
	LudoController ludoController;
	Tab tab;
	
	public PlayerLogin(){
		
	}
	
	private void playerLoggedIn(){
		Connection.newLoginRequest(this, username.getText(), password.getText());
	}
	 
	@FXML
	public void handleLoginButton() {

		Connection.sendMessage(username.getText() + ":" + password.getText(), "LOGIN_REQUEST", "-1");
	}

	@FXML 
	public void handleRegisterButton(){
		
		ludoController.registerDisplay();
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
