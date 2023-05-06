package pl.maciejnierzwicki.moments.config.security.jwt;

import lombok.Data;
import pl.maciejnierzwicki.moments.model.profile.Profile;

@Data
public class JwtResponse {
	private Profile user;
	private String refreshToken;

	public JwtResponse(Profile user, String refreshToken) {
		this.user = user;
		this.refreshToken = refreshToken;
	}
	
}
