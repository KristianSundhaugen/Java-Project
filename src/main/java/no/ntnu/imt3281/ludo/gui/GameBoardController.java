
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
import no.ntnu.imt3281.ludo.client.Connection;

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
	
    private double[][] piecePos = new double[91][2];
	

    public GameBoardController(){
    	ludo = new Ludo();
    	ludo.addDiceListener(Connection.getConnection());
    	ludo.addPieceListener(Connection.getConnection());
    	ludo.addPlayerListener(Connection.getConnection());
    }
    
	/**
	 * Throwing a dice
	 */
    public void throwDiceController(){
    	int i = ludo.throwDice();
    	System.out.println("Dice value: " + i);
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
     * @return the id of the game
     */
	public String getId() {
		return ludo.getId();
	}
	/**
	 * Setting the id of the game from the server
	 * @param gameId
	 */
	public void setId(String gameId) {
    	ludo.setId(gameId);
	}
    
	/**
	 * Loads the image for the pieces
	 * Adds a rectangle on each piece and fill it with the correct image
	 * 
	 */
	public void setUpPieces(int diceValue){
		
		//create a class
		//function that takes player and piece position and returns rectangle
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
				double px = getXPosition(i * 4 + j);
				double py = getYPosition(i * 4 + j);
				pieces[i][j].setX(px);
				pieces[i][j].setY(py);
			}
		}
	}
	
	public double getYPosition(int p){
		
		return piecePos[p][1];
	}
	
	public double getXPosition(int p){
		
		return piecePos[p][0];
	}
	
	public void setPiecePosition(){
		//Red start positions
		piecePos[0][0] = 11.5;
		piecePos[0][1] = 1.5;
		piecePos[1][0] = 12.5;
		piecePos[1][1] = 2.5;
		piecePos[2][0] = 11.5;
		piecePos[2][1] = 3.5;
		piecePos[3][0] = 10.5;
		piecePos[3][1] = 2.5;
		//Blue start positions
		piecePos[4][0] = 11.5;
		piecePos[4][1] = 10.5;
		piecePos[5][0] = 12.5;
		piecePos[5][1] = 11.5;
		piecePos[6][0] = 11.5;
		piecePos[6][1] = 12.5;
		piecePos[7][0] = 10.5;
		piecePos[7][1] = 11.5;
		//Yellow start positions
		piecePos[8][0] = 2.5;
		piecePos[8][1] = 10.5;
		piecePos[9][0] = 3.5;
		piecePos[9][1] = 11.5;
		piecePos[10][0] = 2.5;
		piecePos[10][1] = 12.5;
		piecePos[11][0] = 1.5;
		piecePos[11][1] = 11.5;
		//Green start positions
		piecePos[12][0] = 2.5;
		piecePos[12][1] = 1.5;
		piecePos[13][0] = 3.5;
		piecePos[13][1] = 2.5;
		piecePos[14][0] = 2.5;
		piecePos[14][1] = 3.5;
		piecePos[15][0] = 1.5;
		piecePos[15][1] = 2.5;
		//Rest of the board positions
		piecePos[16][0] = 9;
		piecePos[16][1] = 1;
		piecePos[17][0] = 9;
		piecePos[17][1] = 2;
		piecePos[18][0] = 9;
		piecePos[18][1] = 3;
		piecePos[19][0] = 9;
		piecePos[19][1] = 4;
		piecePos[20][0] = 9;
		piecePos[20][1] = 5;
		piecePos[21][0] = 9;
		piecePos[21][1] = 6;
		piecePos[22][0] = 10;
		piecePos[22][1] = 6;
		piecePos[23][0] = 11;
		piecePos[23][1] = 6;
		piecePos[24][0] = 12;
		piecePos[24][1] = 6;
		piecePos[25][0] = 13;
		piecePos[25][1] = 6;
		piecePos[26][0] = 14;
		piecePos[26][1] = 6;
		piecePos[27][0] = 14;
		piecePos[27][1] = 7;
		piecePos[28][0] = 14;
		piecePos[28][1] = 8;
		piecePos[29][0] = 13;
		piecePos[29][1] = 8;
		piecePos[30][0] = 12;
		piecePos[30][1] = 8;
		piecePos[31][0] = 11;
		piecePos[31][1] = 8;
		piecePos[32][0] = 10;
		piecePos[32][1] = 8;
		piecePos[33][0] = 9;
		piecePos[33][1] = 8;
		piecePos[34][0] = 9;
		piecePos[34][1] = 9;
		piecePos[35][0] = 9;
		piecePos[35][1] = 10;
		piecePos[36][0] = 9;
		piecePos[36][1] = 11;
		piecePos[37][0] = 9;
		piecePos[37][1] = 12;
		piecePos[38][0] = 9;
		piecePos[38][1] = 13;
		piecePos[39][0] = 9;
		piecePos[39][1] = 14;
		piecePos[40][0] = 7;
		piecePos[40][1] = 14;
		piecePos[41][0] = 6;
		piecePos[41][1] = 14;
		piecePos[42][0] = 6;
		piecePos[42][1] = 13;
		piecePos[43][0] = 6;
		piecePos[43][1] = 12;
		piecePos[44][0] = 6;
		piecePos[44][1] = 11;
		piecePos[45][0] = 6;
		piecePos[45][1] = 10;
		piecePos[46][0] = 6;
		piecePos[46][1] = 9;
		piecePos[47][0] = 0;
		piecePos[47][1] = 8;
		piecePos[48][0] = 0;
		piecePos[48][1] = 8;
		piecePos[49][0] = 1;
		piecePos[49][1] = 8;
		piecePos[50][0] = 2;
		piecePos[50][1] = 8;
		piecePos[51][0] = 3;
		piecePos[51][1] = 8;
		piecePos[52][0] = 4;
		piecePos[52][1] = 8;
		piecePos[53][0] = 0;
		piecePos[53][1] = 7;
		piecePos[54][0] = 0;
		piecePos[54][1] = 6;
		piecePos[55][0] = 1;
		piecePos[55][1] = 6;
		piecePos[56][0] = 2;
		piecePos[56][1] = 6;
		piecePos[57][0] = 3;
		piecePos[57][1] = 6;
		piecePos[58][0] = 4;
		piecePos[58][1] = 6;
		piecePos[59][0] = 5;
		piecePos[59][1] = 6;
		piecePos[59][0] = 7;
		piecePos[59][1] = 6;
		piecePos[60][0] = 6;
		piecePos[60][1] = 5;
		piecePos[61][0] = 6;
		piecePos[61][1] = 4;
		piecePos[62][0] = 6;
		piecePos[62][1] = 3;
		piecePos[63][0] = 6;
		piecePos[63][1] = 2;
		piecePos[64][0] = 6;
		piecePos[64][1] = 1;
		piecePos[65][0] = 6;
		piecePos[65][1] = 0;
		piecePos[66][0] = 7;
		piecePos[66][1] = 0;
		piecePos[67][0] = 9;
		piecePos[67][1] = 0;
		piecePos[68][0] = 7;
		piecePos[68][1] = 1;
		piecePos[69][0] = 7;
		piecePos[69][1] = 2;
		piecePos[70][0] = 7;
		piecePos[70][1] = 3;
		piecePos[71][0] = 7;
		piecePos[71][1] = 4;
		piecePos[72][0] = 7;
		piecePos[72][1] = 5;
		piecePos[73][0] = 7;
		piecePos[73][1] = 6;
		piecePos[74][0] = 14;
		piecePos[74][1] = 7;
		piecePos[75][0] = 13;
		piecePos[75][1] = 7;
		piecePos[76][0] = 12;
		piecePos[76][1] = 7;
		piecePos[77][0] = 11;
		piecePos[77][1] = 7;
		piecePos[78][0] = 10;
		piecePos[78][1] = 7;
		piecePos[79][0] = 9;
		piecePos[79][1] = 7;
		piecePos[80][0] = 7;
		piecePos[80][1] = 13;
		piecePos[81][0] = 7;
		piecePos[81][1] = 12;
		piecePos[82][0] = 7;
		piecePos[82][1] = 11;
		piecePos[83][0] = 7;
		piecePos[83][1] = 10;
		piecePos[84][0] = 7;
		piecePos[84][1] = 9;
		piecePos[85][0] = 7;
		piecePos[85][1] = 8;
		piecePos[86][0] = 1;
		piecePos[86][1] = 7;
		piecePos[87][0] = 2;
		piecePos[87][1] = 7;
		piecePos[88][0] = 3;
		piecePos[88][1] = 7;
		piecePos[89][0] = 4;
		piecePos[89][1] = 7;
		piecePos[90][0] = 5;
		piecePos[90][1] = 7;
		piecePos[91][0] = 6;
		piecePos[91][1] = 7;	
	}	
}