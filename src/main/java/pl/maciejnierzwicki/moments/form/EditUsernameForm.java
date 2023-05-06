package pl.maciejnierzwicki.moments.form;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class EditUsernameForm {
	
	@NotBlank(message = "Username cannot be empty.")
	@Size(min = 4, message = "Username must contain at least 4 characters.")
	private String username;

}
