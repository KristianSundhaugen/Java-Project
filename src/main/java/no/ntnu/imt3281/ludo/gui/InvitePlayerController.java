package no.ntnu.imt3281.ludo.gui;

import java.util.ArrayList;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class InvitePlayerController {
	
	@FXML
	private ListView<String> list;
	
	private ArrayList<String> invites = new ArrayList<>();
	
	@FXML
	public void invitePlayer() {
		invites.add(list.getSelectionModel().getSelectedItem());
	}
	
	@FXML
	public void createGameButton() {
		
	}
}
