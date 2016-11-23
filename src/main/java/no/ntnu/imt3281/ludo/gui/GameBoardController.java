
package no.ntnu.imt3281.ludo.gui;



import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import no.ntnu.imt3281.ludo.logic.Ludo;
import no.ntnu.imt3281.ludo.logic.PlayerEvent;
import no.ntnu.imt3281.ludo.server.ChatMessage;
import no.ntnu.imt3281.ludo.server.GameMessage;
import no.ntnu.imt3281.ludo.server.Message;
import no.ntnu.imt3281.ludo.client.Connection;
import no.ntnu.imt3281.ludo.client.Globals;

/**
 * Class holding a game and a chat, doing logic out from messages from the server
 * @author Lasse Sviland
 *
 */
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
    private Label wonLabel;

    @FXML
    private Button sendTextButton;
    
	private Ludo ludo;

    private double[][] piecePos = Globals.getPiecePossitions();
	private int playerNumber;
	private AnchorPane gameBoard;
    private Rectangle[][] pieces;
    
	/**
	 * Constructor for the controller, making local Ludo object and sending it to the connection
	 */
    public GameBoardController(){
    	ludo = new Ludo();
    	Connection.addGame(this);
		pieces = new Rectangle[4][4];
    }
    
    /**
     * Running when FXML have finished loading
     */
    @FXML
    protected void initialize() {
		ImageView[] playersActive = new ImageView[]{
				player1Active,
				player2Active,
				player3Active,
				player4Active};
		for (int i = 0; i < playersActive.length; i++) {
			playersActive[i].setVisible(false);
		}
		throwTheDice.setDisable(true);
    }

	/**
	 * Throwing a dice
	 */
    public void throwDiceButton() {
    	throwTheDice.setDisable(true);
    	Connection.sendMessage("DICE_THROW", "GAME", ludo.getId());
    	setDiceImage(0);
    }
    /**
     * Triggered when the say button is clicked under the chat
     * sending a message to the server about the the chat message
     */
    public void sendMessageButton() {
    	if (textToSay.getText().length() > 0) {
    		String player = ludo.getPlayerName(playerNumber);
    		Connection.sendMessage(player + ":" + textToSay.getText(), "CHAT", ludo.getId());
    		textToSay.setText("");
    	}
    }
	
	/**
	 * Getting a new message sent from the server to this game
	 * @param msg the message
	 */
	public void messageParser(Message msg) {
		Platform.runLater(new Runnable() {
            @Override
            public void run() {
            	if (msg.isChat())
            		chatMessageParser(msg.getChatMessage());
            	else if (msg.isGame())
            		gameMessageParser(msg.getGameMessage());
            }
		});
	}
	
	/**
	 * Parsing a game message
	 * @param msg the message to parse
	 */
	private void gameMessageParser(GameMessage msg) {
		switch (msg.getType()) {
			case "PLAYER_EVENT": runPlayerEvent(msg); 	break;
			case "PIECE_EVENT":  runPieceEvent(msg); 	break;
			case "DICE_EVENT": 	 runDiceEvent(msg); 	break;
			case "PLAYER_JOINED":runPlayerJoined(msg); 	break;
			case "START_GAME":   waitForNextThrow(0); 	break;
		}
	}
	
	/**
	 * Parsing a chat message
	 * @param msg the message to parse
	 */
	private void chatMessageParser(ChatMessage msg) {
		chatArea.appendText(msg.getUsername() + ": " + msg.getMessageContent());
	}
	
	/**
	 * Responding to a player event from the server
	 * Doing actions in the GUI / Ludo game if a player state changes
	 * @param msg the message containing the  event
	 */
	private void runPlayerEvent(GameMessage msg) {
		int player = msg.part(2);
		switch (msg.part(1)) {
			case PlayerEvent.LEFTGAME: 
				ludo.removePlayer(ludo.getPlayerName(player));
				updatePlayerNames();
				break;
			case PlayerEvent.WON: 
				setWinner(player);
				break;
			case PlayerEvent.PLAYING: 
				waitForNextThrow(player); 
				break;
		}
	}
	
	/**
	 * Responding to a piece event from the server
	 * Doing actions in the GUI / Ludo game if a piece is moved
	 * @param msg the message containing the event
	 */
	private void runPieceEvent(GameMessage msg) {
		int player = msg.part(4);
		ludo.movePiece(player, msg.part(1), msg.part(2));
		if (ludo.activePlayer() == player) 
			waitForNextThrow(player);
	}
	
	/**
	 * Responding to a dice event from the server
	 * Doing actions in the GUI / Ludo game if a dice is thrown
	 * @param msg the message containing the event
	 */
	private void runDiceEvent(GameMessage msg) {
		throwDice(msg.part(1), msg.part(2));
	}
	
	/**
	 * Responding to a player join message from the server
	 * Doing actions in the GUI / Ludo game to add the player
	 * @param msg the message containing the message
	 */
	private void runPlayerJoined(GameMessage msg) {
		ludo.addPlayer(msg.getMessageValue());
		updatePlayerNames();
	}

	/**
	 * Showing the win message with the color of the winning player, and the text with the player that won
	 * @param playerNum the player number that have won the game
	 */
	private void setWinner(int playerNum) {
		switch (playerNum) {
			case 0:	wonLabel.setStyle("textFill:RED");break;
			case 1:	wonLabel.setStyle("textFill:BLUE");break;
			case 2:	wonLabel.setStyle("textFill:YELLOW");break;
			case 3:	wonLabel.setStyle("textFill:GREEN");break;
		}			
		wonLabel.setText(ludo.getPlayerName(playerNum) + " won!");
		wonLabel.setVisible(true);
		throwTheDice.setDisable(true);

	}

	/**
	 * Setting a specific player as the active player, hiding dices for the other players, show for the active player
	 * @param playerNum the player number of the active player
	 */
	private void setActivePlayer(int playerNum) {
		ImageView[] playersActive = new ImageView[]{
				player1Active,
				player2Active,
				player3Active,
				player4Active};
		for (int i = 0; i < playersActive.length; i++) {
			if( i == playerNum)
				playersActive[i].setVisible(true);
			else
				playersActive[i].setVisible(false);
		}
	}
	
	/**
	 * Activating the throw button for a specific player
	 * @param player the player to activate for
	 */
	private void activateThrowButton(int player) {
		if (player == this.playerNumber)
			throwTheDice.setDisable(false);
		else
			throwTheDice.setDisable(true);
	}
	
	/**
	 * Waiting for the next player to throw
	 * clearing any action that might be left from last player,
	 * setting the active sign on the active player
	 * @param player the player that should throw
	 */
	private void waitForNextThrow(int player) {
		updatePiecePositions();
		setActivePlayer(player);
		activateThrowButton(player);
		clearPieceMouseClick();
	}
	
	/**
	 * Running when a player is joining the game, adding the player to the ludo game and updating the GUI with the user name
	 */
	private void updatePlayerNames() {
		for (int i = 0; i < ludo.nrOfPlayers(); i++) {
			updatePlayerName(i);
		}
	}
	
	/**
	 * Updating the player name in the gui for a spesiffic player
	 * @param playerNum the player to update the name for
	 */
	private void updatePlayerName(int playerNum){
		Label player = player1Name;
		switch (ludo.nrOfPlayers()) {
			case 1:player = player1Name;break;
			case 2:player = player2Name;break;
			case 3:player = player3Name;break;
			case 4:player = player4Name;break;
		}
		player.setText(ludo.getPlayerName(playerNum));
		if (this.playerNumber == playerNum)
			player.setUnderline(true);
	}
	
	/**
	 * Running when there is a throw dice event from the server, updating the value of the dice
	 * @param diceValue the value of the dice
	 * @param player the player that threw the dice
	 */
	private void throwDice(int diceValue, int player) {
		ludo.throwDice(diceValue);
		setDiceImage(diceValue);
		if (ludo.activePlayer() == player) {
			if (diceValue != 6 && ludo.allHome()) {
				waitForNextThrow(player);
			} else {
				addPieceMouseClick(player, diceValue);
			}
		} else {
			waitForNextThrow(ludo.activePlayer());
		}
	}
	
	/**
	 * Setting the dice image to the dice values sent as a parameter
	 * @param diceValue the value of the dice
	 */
	private void setDiceImage(int diceValue) {
		Image image;
		if (diceValue == 0)
			image = new Image(getClass().getResourceAsStream("/images/rolldice.png"));
		else
			image = new Image(getClass().getResourceAsStream("/images/dice" + diceValue + ".png"));
		diceThrown.setImage(image);
	}

	/**
     * @return the id of the game
     */
	public String getId() {
		return ludo.getId();
	}
	
	/**
	 * Setting the id of the game from the server
	 * @param gameId the id of the game
	 */
	public void setId(String gameId) {
    	ludo.setId(gameId);
	}
    
	/**
	 * Adding the pieces to the board, 4 for each player
	 */
	public void setUpPieces() {
		Image[] pieceImages = new Image[4];
		pieceImages[0] = new Image(getClass().getResourceAsStream("/images/redPiece.png"));
		pieceImages[1] = new Image(getClass().getResourceAsStream("/images/bluePiece.png"));
		pieceImages[2] = new Image(getClass().getResourceAsStream("/images/yellowPiece.png"));
		pieceImages[3] = new Image(getClass().getResourceAsStream("/images/greenPiece.png"));
		
		for(int player = 0; player < 4; player++){
			for(int piece = 0; piece < 4; piece++){
				pieces[player][piece] = new Rectangle(36, 36);
				pieces[player][piece].setFill(new ImagePattern(pieceImages[player]));
				gameBoard.getChildren().add(pieces[player][piece]);
			}
		}
		updatePiecePositions();
	}
	
	/**
	 * Updating the positions of the players pieces out from the positions in the ludo game
	 */
	public void updatePiecePositions() {
		for(int player = 0; player < 4; player++){
			for(int piece = 0; piece < 4; piece++){
				int playerPos = ludo.getPosition(player, piece);
				int piecesAtPos = ludo.piecesAtPosition(player, playerPos);
				if (playerPos == 0)
					piecesAtPos = 1;
				
				int position = ludo.getPieceBoardPos(player, piece);
				
				if (piecesAtPos == 1)
					pieces[player][piece].setX(getXPosition(position));
				else
					pieces[player][piece].setX(getXPosition(position) + piece * 3);
				
				pieces[player][piece].setY(getYPosition(position));
				pieces[player][piece].setEffect(null);
			}
		}
	}
	
	/**
	 * Adding mouse click listeners for the buttons that a player is able to move
	 * @param player the player that should get the listener
	 * @param diceValue the dice value that was thrown
	 */
	public void addPieceMouseClick(int player, int diceValue) {
		for (int piece = 0; piece < 4; piece++) {
			final int finalPiece = piece;
			int from = ludo.getPosition(player, piece);
			int to = ludo.getPosition(player, piece) + diceValue;
			if (from == 0)
				to = 1;
			if (ludo.isValidMove(player, from, to)) {
				pieces[player][piece].setOnMouseClicked(e -> pieceClicked(player, finalPiece));
				addShaddow(pieces[player][piece]);
			}
		}
	}
	
	/**
	 * Adding a shaddow to a image, used to mark the piece that can be moved
	 * @param image the image to add a shaddow to
	 */
	public void addShaddow(Rectangle image){
		DropShadow dropShadow = new DropShadow();
		dropShadow.setRadius(7.0);
		dropShadow.setOffsetX(3.0);
		dropShadow.setOffsetY(3.0);
		dropShadow.setColor(Color.color(0, 0, 0));
		image.setEffect(dropShadow);
		
	}
	
	/**
	 * Removing all mouse click listeners and effect for all pieces in the game
	 */
	public void clearPieceMouseClick() {
		for (int player = 0; player < 4; player++) {
			for (int piece = 0; piece < 4; piece++) {
				pieces[player][piece].setOnMouseClicked(null);
				pieces[player][piece].setEffect(null);
			}
		}
	}
	
	/**
	 * Telling the server when a piece with a click listener was clicked,
	 * removing the listeners
	 * @param player the player that owns the piece
	 * @param piece the piece that was clicked
	 */
	private void pieceClicked(int player, final int piece) {
		Connection.sendMessage("PIECE_CLICK:" + piece + ":" + player, "GAME", getId());
		clearPieceMouseClick();
	}
	
	/**
	 * Returning the Y position on the board out from a game position
	 * @param pos the position in the game
	 * @return the Y collumn on the board
	 */
	public double getYPosition(int pos){
		return piecePos[pos][1] * 48 + 6;
	}
	
	/**
	 * Returning the X position on the board out from a game position
	 * @param pos the position in the game
	 * @return the X row on the board
	 */
	public double getXPosition(int pos){
		return piecePos[pos][0] * 48 + 6;
	}
	
	/**
	 * Setting the number of the player owning the session
	 * then updating the names
	 * @param playerNumber the player number
	 */
	public void setPlayerNumber(int playerNumber) {
		this.playerNumber = playerNumber;
		updatePlayerNames();
	}
	
	/**
	 * Setting the AnchorPane for the game, this is used to add pieces to the game
	 * @param gameBoard the AnchorPane
	 */
	public void setPane(AnchorPane gameBoard) {
		this.gameBoard = gameBoard;
		setUpPieces();
	}
}