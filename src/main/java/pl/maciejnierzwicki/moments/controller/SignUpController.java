package pl.maciejnierzwicki.moments.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pl.maciejnierzwicki.moments.controller.error.FormInvalidException;
import pl.maciejnierzwicki.moments.controller.error.MultiMessageException;
import pl.maciejnierzwicki.moments.controller.error.UserExistsException;
import pl.maciejnierzwicki.moments.converters.UserToProfileConverter;
import pl.maciejnierzwicki.moments.form.SignUpForm;
import pl.maciejnierzwicki.moments.model.profile.Profile;
import pl.maciejnierzwicki.moments.model.user.User;
import pl.maciejnierzwicki.moments.service.UserService;

@RestController
@RequestMapping("/signup")
@CrossOrigin
public class SignUpController {
	
	@Autowired
	private UserService userService;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private UserToProfileConverter userToProfileConverter;
	
	@PostMapping
	public ResponseEntity<Profile> processSignUp(@RequestBody @Valid SignUpForm form, BindingResult result, Errors errors) throws MultiMessageException, UserExistsException {
		if(errors.hasErrors()) {
			throw new FormInvalidException(errors.getAllErrors().get(0).getDefaultMessage());		
		}
		User user;
		user = userService.getByUsername(form.getUsername());
		if(user != null) {
			throw new UserExistsException("User with name " + form.getUsername() + " already exists.");
		}
		user = userService.getByEmail(form.getEmail());
		if(user != null) {
			throw new UserExistsException("User with email " + form.getEmail() + " already exists.");
		}
		
		user = form.toUser(passwordEncoder);
		user = userService.save(user);
		return new ResponseEntity<>(userToProfileConverter.getProfile(user, true), HttpStatus.CREATED);
	}

}
