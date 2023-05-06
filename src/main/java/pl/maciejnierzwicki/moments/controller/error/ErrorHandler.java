package pl.maciejnierzwicki.moments.controller.error;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ErrorHandler {
	
	@ExceptionHandler(value = ResourceNotFoundException.class)
	public ResponseEntity<Error> notFoundError(ResourceNotFoundException ex) {
		return new ResponseEntity<>(new Error(ex.getMessage()), HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(value = FormInvalidException.class)
	public ResponseEntity<String> invalidForm(FormInvalidException ex) {
		return badRequestWithMessage(ex.getMessages()[0]);
	}
	
	@ExceptionHandler(value = UserExistsException.class)
	public ResponseEntity<String> userExists(UserExistsException ex) {
		return badRequestWithMessage(ex.getMessage());
	}
	
	@ExceptionHandler(value = EmailUsedException.class)
	public ResponseEntity<Error> emailUsed(EmailUsedException ex) {
		return new ResponseEntity<>(new Error(ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(value = MethodArgumentNotValidException.class)
	public ResponseEntity<String> invalidArgument(MethodArgumentNotValidException ex) {
		FieldError error = ex.getFieldError();
		return badRequestWithMessage(error != null ?error.getDefaultMessage() : null);
	}
	
	@ExceptionHandler(value = BadCredentialsException.class)
	public ResponseEntity<String> badCredentials(BadCredentialsException ex) {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("INCORRECT_USERNAME_OR_PASSWORD");
	}
	
	private ResponseEntity<String> badRequestWithMessage(String message) {
		return ResponseEntity.badRequest().body(message);
	}
	

}
