package no.ntnu.imt3281.ludo.server;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * A class to hash passwords and check passwords towards hashed values. 
 * It uses a both hashing and salt in a combination. 
 * @author Kristian
 * Based on Stackoverflow code found 24.11.2015
 * http://stackoverflow.com/questions/18142745/how-do-i-generate-a-salt-in-java-for-salted-hash
 */
public class Password {
	
	/**
	 * Static utility class
	 */
	private Password(){}
	
	/**
	 * Retruns a salted and hashed password using provided hash
	 * Side effect: The password is destroyd (char[] is filled with zeros)
	 * 
	 * @param password the password to be hashed
	 * @param salt 	16 bytes salt, obtained with getNextSalt methode
	 * 
	 * @return the hashed password and salt
	 */
	public static byte[] hash(char[] password) {
		PBEKeySpec spec = new PBEKeySpec(password);
		Arrays.fill(password, Character.MIN_VALUE);
		try {
			SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
			return skf.generateSecret(spec).getEncoded();
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			throw new AssertionError("Error while hashing a password: " + e.getMessage(), e);
		} finally {
			spec.clearPassword();
		}
	}

	/**
	 * Returns true if the password and salt given matches det hash in the db.
	 * Side effekt: password is destroyd(cha[] filled with zeros)
	 * 
	 * @param password		the password to be checked
	 * @param salt			the salt used to hash 
	 * @param expectedHash	the expected hash value of the password
	 * 
	 * @return true if the given password and salt matches the hash
	 */
	public static boolean isExpectedPassword(char[] password, byte[] expectedHash){
	byte[] pwdHash = hash(password);
	 Arrays.fill(password, Character.MIN_VALUE);
	 if (pwdHash.length != expectedHash.length) 
		 return false;
	 for(int i = 0; i < pwdHash.length; i++){
		 if(pwdHash[i] != expectedHash[i]) 
			 return false;
	 }
	 return true;
	}
}
