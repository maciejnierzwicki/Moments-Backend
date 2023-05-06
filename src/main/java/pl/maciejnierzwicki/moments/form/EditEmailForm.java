package pl.maciejnierzwicki.moments.form;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import lombok.Data;
import pl.maciejnierzwicki.moments.customformvalidation.FieldMatch;

@Data
@FieldMatch(first = "newpassword", second = "confirmpassword", message = "Both passwords must match.")
public class EditEmailForm {
	
	@NotEmpty(message = "E-mail address cannot be empty.")
	@Email(message = "Invalid format of e-mail address.")
	private String email;

}
