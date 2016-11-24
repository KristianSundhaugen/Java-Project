package no.ntnu.imt3281.ludo.server;

/**
 * Class holding a chat message that is used to get the chat content when messages is passed between client and server
 * @author Lasse Sviland
 *
 */
public class ChatMessage extends Message {
	private String username;
	private String messageContent;
	public ChatMessage(Message msg) {
		super(msg);
		this.username = stringPart(0);
		try {
			this.messageContent = stringPart(1);
		} catch (Exception e) {}
		
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
	public boolean isNewChat() {
		return (stringPart(0).equals("NEW_CHAT_JOIN"));
	}
	
}
