package no.ntnu.imt3281.ludo.gui;

import java.awt.event.ActionEvent;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
/**
 * 
 * @author Simen
 * This class will handle login
 * Check password and username
 */
public class PlayerLogin extends JFrame {
	
	@FXML
	private JButton loginButton;
	
	@FXML
	private JLabel user;
	
	@FXML
	private JLabel pass;
	
	@FXML
	private JTextField username;
	
	@FXML
	private JPasswordField password;
	
	@FXML
	private JLabel errorMessage;
	
	public PlayerLogin(){
		
	}
	
	/**
	 * Handles login button press
	 */
	@FXML
	public void handleLoginButton(ActionEvent event) {
		
		if(username.getText().equals("admin") && password.getPassword().equals("admin")){
			try {
				Parent parent = FXMLLoader.load(getClass().getResource("Login.fxml"));
				Stage stage = new Stage();
				Scene scene = new Scene(parent);
				stage.setScene(scene);
				stage.show();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else {
			errorMessage.setText("Username or password is invalid");			
		}
		
	}
}
