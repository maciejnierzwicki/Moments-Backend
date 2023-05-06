package pl.maciejnierzwicki.moments.controller.error;

public class MultiMessageException extends Exception {

	private static final long serialVersionUID = 1L;
	
	private final String[] messages;
	
	public MultiMessageException(String... messages) {
		this.messages = messages;
	}
	
	public String[] getMessages() {
		return messages;
	}

}
