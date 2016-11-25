package no.ntnu.imt3281.ludo.client;

/**
 * Holding global variables for the ludo game
 * @author Lasse Sviland
 *
 */
public class Globals {

	/**
	 * The port to use for the server
	 */
	public static final int SERVER_PORT = 9090;
	
	/**
	 * The server address
	 */
	public static final String SERVER_ADDRESS = "localhost";

	public static final String LOG_NAME = "LudoLog";
	
	/**
	 * Private constructor to prevent instances of this class
	 */
	private Globals() {}
	
	/**
	 * @return Array containing all piece positions
	 */
	public static double[][] getPiecePossitions() {
	    double[][] piecePos = new double[92][2];
		// Red start positions
		piecePos[0] = new double[]{11.5,1.5};
		piecePos[1] = new double[]{12.5,2.5};
		piecePos[2] = new double[]{11.5,3.5};
		piecePos[3] = new double[]{10.5,2.5};
		// Blue start positions
		piecePos[4] = new double[]{11.5,10.5};
		piecePos[5] = new double[]{12.5,11.5};
		piecePos[6] = new double[]{11.5,12.5};
		piecePos[7] = new double[]{10.5,11.5};
		//Yellow start positions
		piecePos[8] = new double[]{2.5,10.5};
		piecePos[9] = new double[]{3.5,11.5};
		piecePos[10] = new double[]{2.5,12.5};
		piecePos[11] = new double[]{1.5,11.5};
		// Green start positions
		piecePos[12] = new double[]{2.5,1.5};
		piecePos[13] = new double[]{3.5,2.5};
		piecePos[14] = new double[]{2.5,3.5};
		piecePos[15] = new double[]{1.5,2.5};
		// Rest of the board positions
		piecePos[16] = new double[]{8,1};
		piecePos[17] = new double[]{8,2};
		piecePos[18] = new double[]{8,3};
		piecePos[19] = new double[]{8,4};
		piecePos[20] = new double[]{8,5};
		piecePos[21] = new double[]{9,6};
		piecePos[22] = new double[]{10,6};
		piecePos[23] = new double[]{11,6};
		piecePos[24] = new double[]{12,6};
		piecePos[25] = new double[]{13,6};
		piecePos[26] = new double[]{14,6};
		piecePos[27] = new double[]{14,7};
		piecePos[28] = new double[]{14,8};
		piecePos[29] = new double[]{13,8};
		piecePos[30] = new double[]{12,8};
		piecePos[31] = new double[]{11,8};
		piecePos[32] = new double[]{10,8};
		piecePos[33] = new double[]{9,8};
		piecePos[34] = new double[]{8,9};
		piecePos[35] = new double[]{8,10};
		piecePos[36] = new double[]{8,11};
		piecePos[37] = new double[]{8,12};
		piecePos[38] = new double[]{8,13};
		piecePos[39] = new double[]{8,14};
		piecePos[40] = new double[]{7,14};
		piecePos[41] = new double[]{6,14};
		piecePos[42] = new double[]{6,13};
		piecePos[43] = new double[]{6,12};
		piecePos[44] = new double[]{6,11};
		piecePos[45] = new double[]{6,10};
		piecePos[46] = new double[]{6,9};
		piecePos[47] = new double[]{5,8};
		piecePos[48] = new double[]{4,8};
		piecePos[49] = new double[]{3,8};
		piecePos[50] = new double[]{2,8};
		piecePos[51] = new double[]{1,8};
		piecePos[52] = new double[]{0,8};
		piecePos[53] = new double[]{0,7};
		piecePos[54] = new double[]{0,6};
		piecePos[55] = new double[]{1,6};
		piecePos[56] = new double[]{2,6};
		piecePos[57] = new double[]{3,6};
		piecePos[58] = new double[]{4,6};
		piecePos[59] = new double[]{5,6};
		piecePos[60] = new double[]{6,5};
		piecePos[61] = new double[]{6,4};
		piecePos[62] = new double[]{6,3};
		piecePos[63] = new double[]{6,2};
		piecePos[64] = new double[]{6,1};
		piecePos[65] = new double[]{6,0};
		piecePos[66] = new double[]{7,0};
		piecePos[67] = new double[]{8,0};
		piecePos[68] = new double[]{7,1};
		piecePos[69] = new double[]{7,2};
		piecePos[70] = new double[]{7,3};
		piecePos[71] = new double[]{7,4};
		piecePos[72] = new double[]{7,5};
		piecePos[73] = new double[]{7,6};
		piecePos[74] = new double[]{13,7};
		piecePos[75] = new double[]{12,7};
		piecePos[76] = new double[]{11,7};
		piecePos[77] = new double[]{10,7};
		piecePos[78] = new double[]{9,7};
		piecePos[79] = new double[]{8,7};
		piecePos[80] = new double[]{7,13};
		piecePos[81] = new double[]{7,12};
		piecePos[82] = new double[]{7,11};
		piecePos[83] = new double[]{7,10};
		piecePos[84] = new double[]{7,9};
		piecePos[85] = new double[]{7,8};
		piecePos[86] = new double[]{1,7};
		piecePos[87] = new double[]{2,7};
		piecePos[88] = new double[]{3,7};
		piecePos[89] = new double[]{4,7};
		piecePos[90] = new double[]{5,7};
		piecePos[91] = new double[]{6,7};	
		return piecePos;
	}
}
