package pl.maciejnierzwicki.moments.model.passwordresetform;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import lombok.Data;

@Data
public class PasswordResetForm {
	
	@NotEmpty(message = "EMAIL_EMPTY")
	@Email(message = "EMAIL_INVALID")
	private String email;

}
