package pl.maciejnierzwicki.moments.controller.error;

import lombok.Data;

@Data
class Error {
	
	private String[] messages;
	
	public Error(String... messages) {
		this.messages = messages;
	}

}
