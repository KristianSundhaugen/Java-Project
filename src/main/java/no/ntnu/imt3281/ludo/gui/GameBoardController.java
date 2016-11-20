
package no.ntnu.imt3281.ludo.gui;


import javafx.application.Platform;
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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import no.ntnu.imt3281.ludo.logic.Ludo;
import no.ntnu.imt3281.ludo.logic.PlayerEvent;
import no.ntnu.imt3281.ludo.server.GameMessage;
import no.ntnu.imt3281.ludo.client.Connection;
import no.ntnu.imt3281.ludo.client.Globals;

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
    
	private Ludo ludo;

    private double[][] piecePos = Globals.getPiecePossitions();
	

	private int playerNumber;

	private AnchorPane gameBoard;
    
	/**
	 * Constructor for the controller, making local Ludo object and sending it to the connection
	 */
    public GameBoardController(){
    	ludo = new Ludo();
    	Connection.addGame(this);
    }
    
    /**
     * Running when FXML have finished loading
     */
    @FXML
    protected void initialize() {
    	ludo.addDiceListener(Connection.getConnection());
    	ludo.addPieceListener(Connection.getConnection());
    	ludo.addPlayerListener(Connection.getConnection());
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
    public void throwDiceController() {
    	throwTheDice.setDisable(true);
    	Connection.sendMessage("DICE_THROW", "GAME", ludo.getId());
    }

    
	
	/**
	 * Getting a new message sent from the server to this game
	 * @param msg the message
	 */
	public void gameMessage(GameMessage msg) {
		Platform.runLater(new Runnable() {
            @Override
            public void run() {
            	gameMessageParser(msg);
            }
		});
	}
	
	/**
	 * Parsing a game message
	 * @param msg the message to parse
	 */
	private void gameMessageParser(GameMessage msg) {
		System.out.println("GameMessage Conrrller");
		if(msg.isType("PLAYER_EVENT")) {
        	String[] parts = msg.getMessage().split(":");
        	int state = Integer.parseInt(parts[1]);
			int playerNum = Integer.parseInt(parts[2]);
			if (state == PlayerEvent.LEFTGAME) {
				ludo.removePlayer(ludo.getPlayerName(playerNum));
				Label player = player1Name;
				switch (playerNum) {
					case 1:player = player1Name;break;
					case 2:player = player2Name;break;
					case 3:player = player3Name;break;
					case 4:player = player4Name;break;
				}
				player.setText(ludo.getPlayerName(playerNum));
			} else if (state == PlayerEvent.WON) {
				
			} else if (state == PlayerEvent.PLAYING) {
				setActivePlayer(playerNum);
				
			} else if (state == PlayerEvent.WAITING) {
				ImageView playerActive = player1Active;
				switch (ludo.nrOfPlayers()) {
					case 1:playerActive = player1Active;break;
					case 2:playerActive = player2Active;break;
					case 3:playerActive = player3Active;break;
					case 4:playerActive = player4Active;break;
				}	
				playerActive.setVisible(false);
			}
		} else if (msg.isType("PIECE_EVENT")) {
			String[] parts = msg.getMessage().split(":");
        	int fromPos = Integer.parseInt(parts[1]);
			int toPos = Integer.parseInt(parts[2]);
			int player = Integer.parseInt(parts[4]);
			ludo.movePiece(player, fromPos, toPos);
		} else if (msg.isType("DICE_EVENT")) {
			String[] parts = msg.getMessage().split(":");
        	int diceValue = Integer.parseInt(parts[1]);
			int player = Integer.parseInt(parts[2]);
			throwDice(diceValue, player);
		} else if (msg.isType("PLAYER_JOINED")) {
			System.out.println("PLAYER JOIN");
			playerJoin(msg.getMessageValue());
		} else if (msg.isType("START_GAME")) {
			setActivePlayer(0);
			activateThrowButton(0);
		}
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
	 * Running when a player is joining the game, adding the player to the ludo game and updating the GUI with the user name
	 * @param playerName the user name of the new player
	 */
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
	 * Running when there is a throw dice event from the server, updating the value of the dice
	 * @param diceValue the value of the dice
	 * @param player the player that threw the dice
	 */
	private void throwDice(int diceValue, int player) {
		ludo.throwDice(diceValue);
		diceThrown.setImage(new Image(getClass().getResourceAsStream("/images/dice" + diceValue + ".png")));
		if (ludo.activePlayer() == player) {
			// TODO Run function to mark possible possition user can move and add click listeners for those possitions
			setUpPieces();
		}
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
    
	
	
	public void movePiece(int diceValue) {
		setUpPieces();
	}
	/**
	 * Using the pieces position in the ludo object to place
	 * the images of the pictures on the correct locations on the 
	 * ludo board
	 */
	public void setUpPieces(){
		System.out.println("Setup Pieces");
		Rectangle[][] pieces = new Rectangle[4][4];
		Image[] pieceImages = new Image[4];
		pieceImages[0] = new Image(getClass().getResourceAsStream("/images/redPiece.png"));
		pieceImages[1] = new Image(getClass().getResourceAsStream("/images/bluePiece.png"));
		pieceImages[2] = new Image(getClass().getResourceAsStream("/images/greenPiece.png"));
		pieceImages[3] = new Image(getClass().getResourceAsStream("/images/yellowPiece.png"));
		
		for(int player = 0; player < 4; player++){
			for(int piece = 0; piece < 4; piece++){
				pieces[player][piece] = new Rectangle(48, 48);
				pieces[player][piece].setFill(new ImagePattern(pieceImages[player]));
				int position = ludo.getPieceBoardPos(player, piece);
				System.out.println(position);
				pieces[player][piece].setX(getXPosition(position) + piece*8);
				pieces[player][piece].setY(getYPosition(position) + piece*4);
				gameBoard.getChildren().add(pieces[player][piece]);
			}
		}
	}
	public void setMouseClick(int diceValue) {
		/**
		 * pieces[i][j].setOnMouseClicked(new EventHandler<MouseEvent>(){
					
					@Override
		            public void handle(MouseEvent t){
						
						//Send message to client about player clicking on piece
						int fromPos = ludo.getPosition(player, piece);
						int toPos = fromPos + diceValue;
						ludo.movePiece(ludo.activePlayer(), fromPos, toPos);
					}
				});
		 */
	}
	public double getYPosition(int pos){
		return piecePos[pos][1] * 48;
	}
	
	public double getXPosition(int pos){
		return piecePos[pos][0] * 48;
	}
	

	public void setPlayerNumber(int playerNumber) {
		this.playerNumber = playerNumber;
		
	}
	public void setPane(AnchorPane gameBoard) {
		this.gameBoard = gameBoard;
    	setUpPieces();
	}
}