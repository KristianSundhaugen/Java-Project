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
public class LoginController {
	
	@FXML
	private Button loginButton;
	
	@FXML
	private TextField username;
	
	@FXML
	private PasswordField password;
	
	@FXML
	private Label infoLabel;
	
	private LudoController ludoController;
	
	private Tab tab;
	
	/**
	 * The constructor for the controller
	 */
	public LoginController(){
		
	}
	
	/**
	 * Response to the user clicking the login button, sending message to server
	 * if the useranme and password is long enough
	 */
	@FXML
	public void handleLoginButton() {
		if ( username.getText().length() > 1 && password.getText().length() > 1) {
			Connection.setLoginController(this);
			Connection.sendMessage("LOGIN_REQUEST:" + username.getText() + ":" + password.getText(), "USER", "-1");
		} else {
			infoLabel.setText("Username/Password is too short");
		}
	}

	/**
	 * Response to user cliking the register button, message to server
	 * if the useranme and password is long enough
	 */
	@FXML 
	public void handleRegisterButton() {
		if ( username.getText().length() > 1 && password.getText().length() > 1) {
			Connection.setLoginController(this);
			Connection.sendMessage("REGISTER_REQUEST:" + username.getText() + ":" + password.getText(), "USER", "-1");
		} else {
			infoLabel.setText("Username/Password is too short");
		}
	}
	
	/**
	 * Login response from the server, giving erro if the login was not successful
	 * closing login if the user was logged in
	 */
	public void loginResponse(UserMessage umsg) {
		if(umsg.intPart(1) == 1)
			infoLabel.setText("Wrong username or password");
		else {
			Connection.loggedIn(username.getText());
			ludoController.removeTab(tab);
		}
	}
	
	/**
	 * Register response from the server, giving errors if the register was not successful
	 */
	public void registerResponse(UserMessage umsg){
		if(umsg.intPart(1) == 1)
			infoLabel.setText("Username is taken!");
		else if (umsg.intPart(1) == 2)
			infoLabel.setText("Invalid username!");
		else {
			Connection.loggedIn(username.getText());
			ludoController.removeTab(tab);
		}
	}

	/**
	 * Setting the ludo controller, making it possible to access the controller for removing the tab
	 */
	public void setLudoController(LudoController ludoController, Tab tab) {
		this.ludoController = ludoController;
		this.tab = tab;
	}
}
