package pl.maciejnierzwicki.moments.model.passwordresetconfirmform;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.Data;
import pl.maciejnierzwicki.moments.customformvalidation.FieldMatch;

@Data
@FieldMatch(first = "newPassword", second = "confirmPassword", message = "PASSWORDS_MISMATCH")
public class PasswordResetConfirmForm {
	
	private Integer resetCode;
	
	@NotEmpty(message = "PASSWORD_EMPTY")
	@Size(min = 5, message = "PASSWORD_MIN_LENGTH")
	private String newPassword;
	
	@NotEmpty(message = "PASSWORD_EMPTY")
	@Size(min = 5, message = "PASSWORD_MIN_LENGTH")
	private String confirmPassword;

}
