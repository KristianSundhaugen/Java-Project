package no.ntnu.imt3281.ludo.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import no.ntnu.imt3281.I18N.I18N;
import no.ntnu.imt3281.ludo.client.Connection;
import no.ntnu.imt3281.ludo.server.GameMessage;

public class InviteController {

	@FXML
	Label inviteLabel;
	
	private LudoController ludoController;
	private Tab tab;
	private String inviter = "";
	private String gameId;

	/**
	 * Running when FXML have finished loading
	 */
	@FXML
	protected void initialize() {
		inviteLabel.setText(I18N.getBundle().getString("invite.message") + inviter);
	}

	/**
	 * Accepting the invite, sending message to the server that the client want to join the game, closing the tab 
	 */
	public void acceptInvite() {
		Connection.sendMessage("INVITE_ACCEPT", "GAME", gameId);
		ludoController.removeTab(tab);
	}
	
	/**
	 * Ignoring a invite and closint the tab
	 */
	public void denyInvite() {
		ludoController.removeTab(tab);
	}
	
	/**
	 * Updating the information about the invite
	 * @param gameMessage the message containing the invite
	 */
	public void setInviteInfo(GameMessage gameMessage) {
		this.inviter = gameMessage.stringPart(1);
		this.gameId = gameMessage.getId();
		inviteLabel.setText(I18N.getBundle().getString("invite.message") + inviter);
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
