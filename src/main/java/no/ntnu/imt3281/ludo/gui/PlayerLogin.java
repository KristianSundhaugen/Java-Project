package no.ntnu.imt3281.ludo.gui;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class PlayerLogin extends JFrame {
	
	JButton loginButton;
	
	JLabel user;
	JLabel pass;
	
	JTextField username;
	JPasswordField password;
	
	public PlayerLogin(){
		loginButton = new JButton("Login");
		user = new JLabel("Username: ");
		pass = new JLabel("Password: ");
		username = new JTextField();
		password = new JPasswordField();
	}
}
