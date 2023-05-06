package pl.maciejnierzwicki.moments.form;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import lombok.Data;

@Data
public class SignInForm {
	
	@NotBlank(message = "USERNAME_EMPTY")
	protected String username;
	
	@NotEmpty(message = "PASSWORD_EMPTY")
	protected String password;
}
