package pl.maciejnierzwicki.moments.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pl.maciejnierzwicki.moments.config.security.jwt.JwtUtils;
import pl.maciejnierzwicki.moments.config.security.jwt.refresh.RefreshToken;
import pl.maciejnierzwicki.moments.config.security.jwt.refresh.RefreshTokenService;
import pl.maciejnierzwicki.moments.config.security.jwt.refresh.TokenRefreshException;
import pl.maciejnierzwicki.moments.config.security.jwt.refresh.TokenRefreshRequest;
import pl.maciejnierzwicki.moments.config.security.jwt.refresh.TokenRefreshResponse;
import pl.maciejnierzwicki.moments.model.user.User;


@RequestMapping("/refreshtoken")
@RestController
public class RefreshTokenController {
	
	@Autowired
	private JwtUtils jwtUtils;
	@Autowired
	private RefreshTokenService refreshTokenService;
	
	
	  @PostMapping
	  public ResponseEntity<TokenRefreshResponse> processRefreshToken(@Valid @RequestBody TokenRefreshRequest request) {
		  
	    String refreshTokenStr = request.getRefreshToken();

	    RefreshToken refreshToken = refreshTokenService.findByToken(refreshTokenStr);
	 
	    
	    if(refreshToken == null) {
	    	throw new TokenRefreshException(refreshTokenStr, "Refresh token is not in database!");
	    }
	    
	    refreshToken = refreshTokenService.verifyExpiration(refreshToken);
	    User user = refreshToken.getUser();
	    String token = jwtUtils.generateJwtToken(user);
	    return ResponseEntity.ok(new TokenRefreshResponse(token, refreshTokenStr));
	 
	  }

}
