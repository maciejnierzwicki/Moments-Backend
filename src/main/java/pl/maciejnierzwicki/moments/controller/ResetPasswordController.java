package pl.maciejnierzwicki.moments.controller;

import java.util.Date;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import pl.maciejnierzwicki.moments.controller.error.FormInvalidException;
import pl.maciejnierzwicki.moments.mail.MailService;
import pl.maciejnierzwicki.moments.model.passwordresetcode.PasswordResetCode;
import pl.maciejnierzwicki.moments.model.passwordresetconfirmform.PasswordResetConfirmForm;
import pl.maciejnierzwicki.moments.model.passwordresetform.PasswordResetForm;
import pl.maciejnierzwicki.moments.model.user.User;
import pl.maciejnierzwicki.moments.service.PasswordResetCodesService;
import pl.maciejnierzwicki.moments.service.UserService;
import pl.maciejnierzwicki.moments.utils.NumberUtils;

@RestController
@RequestMapping("/resetpassword")
@CrossOrigin
@Slf4j
public class ResetPasswordController {
	
	@Autowired
	private MailService emailUtils;
	@Autowired
	private NumberUtils numberUtils;
	@Autowired
	private UserService userService;
	@Autowired
	private PasswordResetCodesService passwordResetCodesService;
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	
	@PostMapping
	public ResponseEntity<HttpStatus> processPasswordResetRequest(@RequestBody @Valid PasswordResetForm form, BindingResult result, Errors errors) throws FormInvalidException {
		if(errors.hasErrors()) {
			throw new FormInvalidException(errors.getAllErrors().get(0).getDefaultMessage());		
		}
		log.info("Email: " + form.getEmail());
		User user = userService.getByEmail(form.getEmail());
		if(user == null) return ResponseEntity.notFound().build();
		Integer code = numberUtils.generate6DigitsCode();
		PasswordResetCode passwordResetCode = new PasswordResetCode();
		passwordResetCode.setCode(code);
		passwordResetCode.setUser(user);
		passwordResetCodesService.save(passwordResetCode);
		emailUtils.sendPasswordResetMail(passwordResetCode);
		return ResponseEntity.ok().build();
	}
	
	@PostMapping("/confirm")
	public ResponseEntity<HttpStatus> processPasswordResetConfirmRequest(@RequestBody @Valid PasswordResetConfirmForm form, BindingResult result, Errors errors) throws FormInvalidException {
		if(errors.hasErrors()) {
			throw new FormInvalidException(errors.getAllErrors().get(0).getDefaultMessage());		
		}
		PasswordResetCode passwordResetCode = passwordResetCodesService.getByCode(form.getResetCode());
		if(passwordResetCode == null || isExpired(passwordResetCode)) return ResponseEntity.notFound().build();
		User user = passwordResetCode.getUser();
		user.setPassword(passwordEncoder.encode(form.getNewPassword()));
		userService.save(user);
		return ResponseEntity.ok().build();
	}
	
	private boolean isExpired(@NonNull PasswordResetCode passwordResetCode) {
		Date currentDate = new Date();
		Date codeCreationDate = passwordResetCode.getCreationDate();
		long diff = currentDate.getTime() - codeCreationDate.getTime();
		log.info("diff: " + diff);
		return diff > 60000;
	}

}
