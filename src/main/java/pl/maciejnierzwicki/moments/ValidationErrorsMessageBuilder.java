package pl.maciejnierzwicki.moments;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.validation.ObjectError;

@Component
public class ValidationErrorsMessageBuilder {
	
	public String[] getMessagesArray(List<ObjectError> errors) {
		String[] messages = new String[errors.size()];
		for(int i = 0; i < errors.size(); i++) {
			messages[i] = errors.get(i).getDefaultMessage();
		}
		return messages;
	}

}
