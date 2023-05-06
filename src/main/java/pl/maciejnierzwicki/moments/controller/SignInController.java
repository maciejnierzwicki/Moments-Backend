package pl.maciejnierzwicki.moments.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pl.maciejnierzwicki.moments.config.security.jwt.JwtResponse;
import pl.maciejnierzwicki.moments.config.security.jwt.JwtUtils;
import pl.maciejnierzwicki.moments.config.security.jwt.refresh.RefreshToken;
import pl.maciejnierzwicki.moments.config.security.jwt.refresh.RefreshTokenService;
import pl.maciejnierzwicki.moments.converters.UserToProfileConverter;
import pl.maciejnierzwicki.moments.form.SignInForm;
import pl.maciejnierzwicki.moments.model.user.User;

@RestController
@RequestMapping("/signin")
@CrossOrigin
public class SignInController {
	
	@Autowired
    private AuthenticationManager authenticationManager;
	@Autowired
    private JwtUtils jwtUtils;
	@Autowired
	private RefreshTokenService refreshTokenService;
	@Autowired
	private UserToProfileConverter userToProfileConverter;
	
	@PostMapping
	public ResponseEntity<JwtResponse> processLogin(@RequestBody @Valid SignInForm form) {
		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(form.getUsername(), form.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		User user = (User) authentication.getPrincipal();
            

		String token = jwtUtils.generateJwtToken(user);
		RefreshToken refreshToken = refreshTokenService.createAndSaveRefreshToken(user);
		JwtResponse response = new JwtResponse(userToProfileConverter.getProfile(user, true), refreshToken.getToken());
		
		return ResponseEntity.ok()
				.header(HttpHeaders.AUTHORIZATION, token)
                .body(response);
    }
}
