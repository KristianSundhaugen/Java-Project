package no.ntnu.imt3281.ludo.server;

import java.util.logging.Logger;

import no.ntnu.imt3281.ludo.client.Globals;

/**
 * @author Simen
 * A class for sending login messages to the server
 */
public class UserMessage extends Message {
    private static Logger logger = Logger.getLogger(Globals.LOG_NAME);

	
	private String loginType;
	private String loginValue;
	
	/**
	 * 
	 * @param message, has the message
	 */
	public UserMessage(Message message) {
		super(message);
		
		this.loginType = stringPart(0);
		try {
			this.loginValue = stringPart(1);
		} catch (Exception e) {
			logger.throwing(this.getClass().getName(), "UserMessage", e);
		}
	}
	
	/**
	 * @return loginType
	 */
	public String getLoginType() {
		return this.loginType;
	}
	
	/**
	 * @return loginValue
	 */
	public String getLoginValue() {
		return this.loginValue;
	}

	/**
	 * @return boolean, if it is a login request
	 */
	public boolean isLoginRequest() {
		return loginType.equals("LOGIN_REQUEST");
	}
	
	/**
	 * @return boolean, if it is a register request
	 */
	public boolean isRegisterRequest() {
		return loginType.equals("REGISTER_REQUEST");
	}
	
	/**
	 * @return boolean, if it is a login response
	 */
	public boolean isLoginRespons() {
		return loginType.equals("LOGIN_RESPONSE");
	}
	
	/**
	 * @return boolean, if it is a register request
	 */
	public boolean isRegisterResponse() {
		return loginType.equals("REGISTER_RESPONSE");
	}
}
