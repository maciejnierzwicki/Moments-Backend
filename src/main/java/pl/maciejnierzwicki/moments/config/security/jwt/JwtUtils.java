package pl.maciejnierzwicki.moments.config.security.jwt;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import pl.maciejnierzwicki.moments.model.user.User;

@Component
@PropertySource(value = {"classpath:application.yml"})
public class JwtUtils {
	
	private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

	@Value("${moments.auth.jwtSecret}")
	private String jwtSecret;

	@Value("${moments.auth.jwtExpirationMs}")
	private int jwtExpirationMs;
	
	public String generateJwtToken(User userPrincipal) {
		return generateTokenFromUsername(userPrincipal.getUsername());
	}
	
	public String generateTokenFromUsername(String username) {
		return Jwts.builder().setSubject(username).setIssuedAt(new Date())
		        .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs)).signWith(SignatureAlgorithm.HS512, jwtSecret)
		        .compact();
	}

	public String getUserNameFromJwtToken(String token) {
		return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
	}

	public boolean validateJwtToken(String authToken) {
		try {
			Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
			return true;
		} catch (SignatureException e) {
			logger.debug("Invalid JWT signature: {}", e.getMessage());
		} catch (MalformedJwtException e) {
			logger.debug("Invalid JWT token: {}", e.getMessage());
		} catch (ExpiredJwtException e) {
			logger.debug("JWT token is expired: {}", e.getMessage());
		} catch (UnsupportedJwtException e) {
			logger.debug("JWT token is unsupported: {}", e.getMessage());
		} catch (IllegalArgumentException e) {
			logger.debug("JWT claims string is empty: {}", e.getMessage());
		}

		return false;
	}

}
