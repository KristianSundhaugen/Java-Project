package no.ntnu.imt3281.ludo.gui;

import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import no.ntnu.imt3281.ludo.client.Connection;
import no.ntnu.imt3281.ludo.server.GameMessage;

public class InvitePlayerController {
	
	@FXML
	private ListView<String> list;
	
	private ArrayList<String> invites = new ArrayList<>();

	private Tab tab;

	private LudoController ludoController;
	/**
	 * Doing a request to the server asking for a list of available chats
	 */
	@FXML
    protected void initialize() {
		Connection.newPlayerListRequest(this);
    }
	@FXML
	public void invitePlayer() {
		invites.add(list.getSelectionModel().getSelectedItem());
		list.getItems().remove(list.getSelectionModel().getSelectedItem());
		if (invites.size() >= 3)
			createGameButton();
	}
	
	/**
	 * When the user is clicking the create game button or when the user have invited the maximum amount of players
	 */
	@FXML
	public void createGameButton() {
		String inviteString = "";
		for (String invite : invites) {
			inviteString += ":" + invite;
		}
		Connection.sendMessage("PRIVATE_GAME_REQUEST" + inviteString, "GAME", "-1");
		ludoController.removeTab(tab);
	}
	
	/**
	 * Adding player names to the list when we get a response from the server with the player names
	 * @param gameMessage the message from the server
	 */
	public void playerListResponse(GameMessage gameMessage) {
		ObservableList<String> items = FXCollections.observableArrayList ();
		for (String playerName : gameMessage.getMessage().split(":"))
			if (!playerName.equals("PLAYER_LIST_RESPONSE")) 
				items.add(playerName);	
		list.setItems(items);
	}
	
	/**
	 * Storing the reference to the ludo controller and the tab this window belongs to
	 * @param ludoController the ludo controller that started this window
	 * @param tab the tab this window belongs to
	 */
	public void setLudoController(LudoController ludoController, Tab tab) {
		this.ludoController = ludoController;
		this.tab = tab;
	}
}
