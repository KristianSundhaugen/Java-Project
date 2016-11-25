package no.ntnu.imt3281.ludo.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import no.ntnu.imt3281.I18N.I18N;
import no.ntnu.imt3281.ludo.client.Connection;
import no.ntnu.imt3281.ludo.server.ChatMessage;


/**
 * Window to contain a list over available chats to join
 * @author Lasse Sviland
 *
 */
public class ListRoomsController {
	
	public ListRoomsController() {
		
	}
	@FXML
	private ListView<String> list;
	private LudoController ludoController;
	private Tab tab;
	
	/**
	 * Doing a request to the server asking for a list of available chats
	 */
	@FXML
	protected void initialize() {
		Connection.newChatListRequest(this);
	}
	
	/**
	 * Triggered when a user select a chat channel to join, this tab is closed and 
	 * a new chat is opened with the selected chat
	 */
	@FXML
	private void selectChannel() {
		String[] parts = list.getSelectionModel().getSelectedItem().split("\t\t");
		String id = parts[0].replaceAll("Id: ", "");
		String name = parts[1].replaceAll("Name: ", "");
		
		Connection.sendMessage("NEW_CHAT_JOIN", "CHAT", id);
		ludoController.removeTab(tab);
		ludoController.createNewChatTab(id, name);
	}
	
	/**
	 * Adding the chats that the user can join to the list
	 * @param chatMessage the chat message containing the list of chats
	 */
	public void listResponse(ChatMessage chatMessage) {
		ObservableList<String> items = FXCollections.observableArrayList ();
		for (String room : chatMessage.getMessage().split(":")) {
			if (!room.equals("LIST_ROOMS_RESPONSE")) {
				String[] parts = room.split("-");
				String name = room.substring(parts[0].length() + parts[1].length() + 2);
				String idStr = I18N.getBundle().getString("chatlist.id");
				String nameStr = I18N.getBundle().getString("chatlist.name");
				String chattersStr = I18N.getBundle().getString("chatlist.chatters");

				items.add(idStr + ": " + parts[0] + "\t\t" + nameStr + ": " + name + "\t\t" + chattersStr + ": " + parts[1]);
			}
		}
		list.setItems(items);
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
