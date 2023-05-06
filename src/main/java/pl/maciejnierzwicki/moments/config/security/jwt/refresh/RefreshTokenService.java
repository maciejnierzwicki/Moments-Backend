package pl.maciejnierzwicki.moments.config.security.jwt.refresh;

import java.time.Instant;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pl.maciejnierzwicki.moments.model.user.User;
import pl.maciejnierzwicki.moments.repository.RefreshTokenRepository;
import pl.maciejnierzwicki.moments.repository.UserRepository;

@Service
public class RefreshTokenService {
	
	@Value("${moments.auth.jwtRefreshExpirationMs}")
	private Long refreshTokenDurationMs;

	@Autowired
	private RefreshTokenRepository refreshTokenRepository;

	@Autowired
	private UserRepository userRepository;

	public RefreshToken findByToken(String token) {
		return refreshTokenRepository.findByToken(token).orElse(null);
	}

	public RefreshToken createAndSaveRefreshToken(User user) {
		RefreshToken refreshToken = new RefreshToken();
 
		refreshToken.setUser(user);
		refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
		refreshToken.setToken(UUID.randomUUID().toString());

		refreshToken = refreshTokenRepository.save(refreshToken);
		return refreshToken;
	}

	public RefreshToken verifyExpiration(RefreshToken token) {
		if(token.getExpiryDate().compareTo(Instant.now()) < 0) {
			refreshTokenRepository.delete(token);
			throw new TokenRefreshException(token.getToken(), "Refresh token was expired. Please make a new signin request");
		}
		return token;
	}

	@Transactional
	public int deleteByUser(User user) {
		return refreshTokenRepository.deleteByUser(user);
	}
}
