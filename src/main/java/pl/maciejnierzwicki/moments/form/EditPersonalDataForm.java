package pl.maciejnierzwicki.moments.form;

import javax.validation.constraints.NotEmpty;

import lombok.Data;

@Data
public class EditPersonalDataForm {
	
	@NotEmpty(message = "E-mail address cannot be empty.")
	private String email;
}
