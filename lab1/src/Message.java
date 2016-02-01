//package lab1;

public class Message {
	private String sender, receiver, message;
	private boolean broadcast;
	
	public Message(String sender, String receiver) {
		this.sender = sender;
		this.receiver = receiver;
		broadcast = false;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}
	
	public String getSender() {
		return sender;
	}
	
	public String getReceiver() {
		return receiver;
	}
	
	public void changeSender(String sender) {
		this.sender = sender;
	}
	
	public void setBroadcast() {
		broadcast = true;
	}
	
	public boolean fromBroadcast() {
		return broadcast;
	}
}
