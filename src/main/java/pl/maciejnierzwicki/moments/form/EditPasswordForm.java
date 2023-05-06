package pl.maciejnierzwicki.moments.form;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.Data;
import pl.maciejnierzwicki.moments.customformvalidation.FieldMatch;

@Data
@FieldMatch(first = "newpassword", second = "confirmpassword", message = "Both passwords must match.")
public class EditPasswordForm {
	
	@NotEmpty(message = "Password cannot be empty.")
	private String oldPassword;
	
	@NotEmpty(message = "Password cannot be empty.")
	@Size(min = 5, message = "Password must contain at least 5 characters.")
	private String newPassword;
	
	@NotEmpty(message = "Password cannot be empty.")
	@Size(min = 5, message = "Password must contain at least 5 characters.")
	private String confirmPassword;

}
