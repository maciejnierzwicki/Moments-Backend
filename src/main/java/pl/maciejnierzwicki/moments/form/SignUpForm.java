package pl.maciejnierzwicki.moments.form;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.Data;
import pl.maciejnierzwicki.moments.customformvalidation.FieldMatch;
import pl.maciejnierzwicki.moments.model.user.User;

@Data
@FieldMatch(first = "password", second = "confirmpassword", message = "PASSWORDS_MISMATCH")
public class SignUpForm {
	
	@NotBlank(message = "USERNAME_EMPTY")
	@Size(min = 4, message = "USERNAME_MIN_LENGTH")
	protected String username;
	
	@NotEmpty(message = "PASSWORD_EMPTY")
	@Size(min = 5, message = "PASSWORD_MIN_LENGTH")
	protected String password;
	
	@NotEmpty(message = "PASSWORD_EMPTY")
	@Size(min = 5, message = "PASSWORD_MIN_LENGTH")
	protected String confirmpassword;
	
	@NotEmpty(message = "EMAIL_EMPTY")
	@Email(message = "EMAIL_INVALID")
	protected String email;
	
	public User toUser(PasswordEncoder encoder) {
		User user = new User(username, encoder.encode(password));
		user.setEmail(email);
		return user;
	}
}
