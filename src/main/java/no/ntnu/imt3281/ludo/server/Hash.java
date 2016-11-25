package no.ntnu.imt3281.ludo.server;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Logger;

import no.ntnu.imt3281.ludo.client.Globals;

/**
 * A class to hash passwords and check passwords towards hashed values. 
 * It uses a both hashing and salt in a combination. 
 * @author Lasse Sviland
 * Based on http://howtodoinjava.com/security/how-to-generate-secure-password-hash-md5-sha-pbkdf2-bcrypt-examples/
 */
public class Hash {
	private static Logger logger = Logger.getLogger(Globals.LOG_NAME);

	private static final String salt = "36afca2100f647898dc854d70d4d6e09";

	/**
	 * Uses MD5 hashing to hash a string with a salt and returning the result as a string
	 * @param password the password that should be hashed
	 * @return the hashed string
	 */
	public static String md5(String password) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update((password+salt).getBytes());
			byte[] bytes = md.digest();
			StringBuilder sb = new StringBuilder();
			for(int i=0; i< bytes.length; i++)
				sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
			return sb.toString();
		} catch (NoSuchAlgorithmException e) { 
			logger.throwing(Hash.class.getClass().getName(), "md5", e);
		}
		return null;
	}
}
