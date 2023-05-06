package pl.maciejnierzwicki.moments.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import pl.maciejnierzwicki.moments.config.security.jwt.refresh.RefreshToken;
import pl.maciejnierzwicki.moments.model.user.User;

@Repository
public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

	void delete(RefreshToken token);
	
	int deleteByUser(User user);

}
