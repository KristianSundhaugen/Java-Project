
package no.ntnu.imt3281.ludo.gui;


/**
 * Sample Skeleton for 'GameBoard.fxml' Controller Class
 */

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

import no.ntnu.imt3281.ludo.logic.Ludo;
import no.ntnu.imt3281.ludo.server.GameMessage;
import no.ntnu.imt3281.ludo.server.Message;

public class GameBoardController {
	
    @FXML
    private Label player1Name;

    @FXML
    private ImageView player1Active;

    @FXML
    private Label player2Name;

    @FXML
    private ImageView player2Active;

    @FXML
    private Label player3Name;
    
    @FXML
    private ImageView player3Active;

    @FXML
    private Label player4Name;

    @FXML
    private ImageView player4Active;
    
    @FXML
    private ImageView diceThrown;
    
    @FXML
    private Button throwTheDice;

    @FXML
    private TextArea chatArea;

    @FXML
    private TextField textToSay;

    @FXML
    private Button sendTextButton;
    
	private Ludo ludo = new Ludo();
    
	private String id;

    public GameBoardController(){
    	
    	//playerPiece[i][ii].setX(corner.points[i*4+ii].getX()-8+ii*4
    	//playerPiece[i][ii].setY(corner.points[i*4+ii].getX()-2+ii*2
    }
    
	/**
	 * Throwing a dice
	 */
    public void throwDiceController(){
    	int i = ludo.throwDice();
    	System.out.println("Dice value: " + i);
    }

    /**
     * @return the id of the game
     */
	public String getId() {
		return this.id;
	}
	
	/**
	 * Getting a new message sent from the server to this game
	 * @param msg the message
	 */
	public void gameMessage(GameMessage msg) {
		if(msg.isType("NEW_JOINED_GAME")) {
			
		} else if (msg.isType("PLAYER_JOINED")){
			playerJoin(msg.getMessageValue());
		} else if (msg.isType("START_GAME")){
			
		} else if (msg.isType("DICE_THROW")){
			
		} else if (msg.isType("PLAYER_MOVE")){
			
		} else if (msg.isType("CHANGE_TURN")){
			
		} else if (msg.isType("PLAYER_DISCONNECT")){
			
		} else if (msg.isType("GAME_FINISHED")){
			
		} else if (msg.isType("PLAYER_JOINED")){
			
		}
	}
	private void playerJoin(String playerName) {
		ludo.addPlayer(playerName);
		Label player = player1Name;
		switch (ludo.nrOfPlayers()) {
			case 1:player = player1Name;break;
			case 2:player = player2Name;break;
			case 3:player = player3Name;break;
			case 4:player = player4Name;break;
		}
		player.setText(playerName);
	}

	/**
	 * Setting the id of the game from the server
	 * @param gameId
	 */
	public void setId(String gameId) {
    	this.id = id;		
	}
    
}