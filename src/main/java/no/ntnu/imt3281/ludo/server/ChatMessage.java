package no.ntnu.imt3281.ludo.server;

/**
 * Class holding a chat message that is used to get the chat content when messages is passed between client and server
 * @author Lasse Sviland
 *
 */
public class ChatMessage {
	private Message msg;
	private String username;
	private String messageContent;
	public ChatMessage(Message msg) {
		this.msg = msg;
		String[] messageParts = msg.getMessage().split(":");
		this.username = messageParts[0];
		this.messageContent = messageParts[1];
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
	 * @return the client that sent the message
	 */
	public ServerClient getClient() {
		return msg.getClient();
	}
	
}
