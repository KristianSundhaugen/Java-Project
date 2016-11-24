package no.ntnu.imt3281.ludo.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import no.ntnu.imt3281.ludo.client.Connection;

public class NewChatController {
	@FXML
	private TextField nameInput;
	private LudoController ludoController;
	private Tab tab;
	
	@FXML
	public void createGame() {
		if ( nameInput.getText().length() > 0) {
			Connection.getConnection().setLudoController(ludoController);
			Connection.sendMessage("CREATE:" + nameInput.getText(), "CHAT", "-1");
			ludoController.removeTab(tab);
		}
 	}
	
	
	
	/**
	 * Setting the ludo controller with the tab bar as well as the tab this controller belongs to
	 * @param ludoController the ludo controller
	 * @param tab the tab this controller belongs to
	 */
	public void setLudoController(LudoController ludoController, Tab tab) {
		this.ludoController = ludoController;
		this.tab = tab;
	}
	
}
