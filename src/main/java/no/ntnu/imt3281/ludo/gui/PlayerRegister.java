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
import javafx.stage.Stage;

public class PlayerRegister {

	@FXML
	private JButton registerButton;
	
	@FXML
	private JTextField playerName;
	
	@FXML
	private JPasswordField playerPassword;
	
	@FXML
	private JLabel registerMessage;
	
	/**
	 * Checks if both username and password has been filled out
	 * If username does not exist in database, put them in and go to login screen
	 * @param event
	 */
	@FXML
	public void handleRegisterButton(ActionEvent event){
		//check if username is already in database
		if(!playerName.getText().equals("") && !playerPassword.getPassword().equals("")){
			registerMessage.setText("User is registered");
			try {
				Parent parent = FXMLLoader.load(getClass().getResource("Login.fxml"));
				Stage stage = new Stage();
				Scene scene = new Scene(parent);
				stage.setScene(scene);
				stage.show();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
