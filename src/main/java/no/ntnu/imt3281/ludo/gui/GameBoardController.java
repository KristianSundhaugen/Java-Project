
package no.ntnu.imt3281.ludo.gui;


import javafx.event.Event;
import javafx.event.EventHandler;

/**
 * Sample Skeleton for 'GameBoard.fxml' Controller Class
 */

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
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
    
	/**
	 * Loads the image for the pieces
	 * Adds a rectangle on each piece and fill it with the  correct image
	 * 
	 * Setting the position for each piece is not yet implemented
	 */
	public void setUpPieces(int diceValue){
		
		
		Rectangle[][] pieces = new Rectangle[4][4];
		Image[] pieceImages = new Image[4];
		pieceImages[0] = new Image(getClass().getResourceAsStream("/images/redPiece.png"));
		pieceImages[1] = new Image(getClass().getResourceAsStream("/images/bluePiece.png"));
		pieceImages[2] = new Image(getClass().getResourceAsStream("/images/greenPiece.png"));
		pieceImages[3] = new Image(getClass().getResourceAsStream("/images/yellowPiece.png"));
		
		for(int i = 0; i < 4; i++){
			for(int j = 0; j < 4; j++){
				pieces[i][j] = new Rectangle(48, 48);
				pieces[i][j].setFill(new ImagePattern(pieceImages[i]));
				//how to place the pieces on each start position?
				pieces[i][j].setX(0);
				pieces[i][j].setY(0);
				
				//there is probably a better way?
				final int player = i;
				final int piece = j;
				pieces[i][j].setOnMouseClicked(new EventHandler<MouseEvent>() {
					
					@Override
					public void handle(MouseEvent event) {
											
						int startPos = ludo.getPosition(player, piece);
						int endPos = startPos + diceValue;
						ludo.movePiece(player, startPos, endPos);
					}
				});
			}
		}
	}
}