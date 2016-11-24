package no.ntnu.imt3281.ludo.gui;

import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import no.ntnu.imt3281.ludo.client.Connection;

public class PlayerRegister {

	@FXML
	private Button registerButton;
	
	@FXML
	private TextField username;
	
	@FXML
	private PasswordField password;
	
	@FXML
	private Label registerMessage;
	
	/**
	 * 
	 */
	@FXML
	public void handleRegisterButton(){
		
		Connection.sendMessage(username.getText(), "REGISTER_REQUEST", "-1");
	}
}
