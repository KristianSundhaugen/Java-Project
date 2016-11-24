package no.ntnu.imt3281.ludo.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.image.ImageView;
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
    	inviteLabel.setText("You have been invited to a game by " + inviter);
    }
	public void acceptInvite() {
		Connection.sendMessage("INVITE_ACCEPT", "GAME", gameId);
		ludoController.removeTab(tab);
	}
	
	public void denyInvite() {
		ludoController.removeTab(tab);
	}
	
	public void setInviteInfo(GameMessage gameMessage) {
		this.inviter = gameMessage.stringPart(1);
		this.gameId = gameMessage.getId();
    	inviteLabel.setText("You have been invited to a game by " + inviter);
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
