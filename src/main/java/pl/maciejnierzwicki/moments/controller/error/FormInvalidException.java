package pl.maciejnierzwicki.moments.controller.error;

public class FormInvalidException extends MultiMessageException {
	
	private static final long serialVersionUID = 1L;

	public FormInvalidException(String... messages) {
		super(messages);
	}

}
