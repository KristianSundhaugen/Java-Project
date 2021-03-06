package no.ntnu.imt3281.ludo.server;

import java.util.logging.Logger;

import no.ntnu.imt3281.ludo.client.Globals;

/**
 * Class holding a chat message that is used to get the chat content when messages is passed between client and server
 * @author Lasse Sviland
 *
 */
public class ChatMessage extends Message {
    private static Logger logger = Logger.getLogger(Globals.LOG_NAME);

	private String username;
	private String messageContent;
	public ChatMessage(Message msg) {
		super(msg);
		this.username = stringPart(0);
		try {
			this.messageContent = stringPart(1);
		} catch (Exception e) {
			logger.throwing(this.getClass().getName(), "ChatMessage", e);
		}
		
	}
	public ChatMessage(String msg, String id) {
		super(msg, "CHAT", id);
		this.username = stringPart(0);
		try {
			this.messageContent = stringPart(1);
		} catch (Exception e) {
			logger.throwing(this.getClass().getName(), "ChatMessage", e);
		}
	}

	/** 
	 * @return the id of the game sent from the server
	 */
	public String getUsername() {
		return username;
	}	
	
	/** 
	 * @return the messageContent
	 */
	public String getMessageContent() {
		return messageContent;
	}
	
	/**
	 * @return if it is a request for getting a list of chats
	 */
	public boolean isListRequest() {
		return (getId().equals("-1") && getMessage().equals("LIST"));
	}
	
	/**
	 * @return if it is a response with possible chats to join
	 */
	public boolean isListResponse() {
		return (stringPart(0).equals("LIST_ROOMS_RESPONSE"));
	}

	/**
	 * @return if it is a request to join a new chat
	 */
	public boolean isNewChatJoin() {
		return (stringPart(0).equals("NEW_CHAT_JOIN"));
	}
	
	/**
	 * @return if it is a request to create a new chat
	 */
	public boolean isNewChat() {
		return (stringPart(0).equals("CREATE"));
	}
	/**
	 * @return if it is a request to create a new chat
	 */
	public boolean isChatJoin() {
		return (stringPart(0).equals("CHAT_JOINED"));
	}	
}
