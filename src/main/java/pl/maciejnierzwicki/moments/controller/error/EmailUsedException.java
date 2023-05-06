package pl.maciejnierzwicki.moments.controller.error;

public class EmailUsedException extends Exception {

	private static final long serialVersionUID = 1L;

	public EmailUsedException(String message) {
		super(message);
	}
}
