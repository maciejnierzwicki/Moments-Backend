package pl.maciejnierzwicki.moments.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import pl.maciejnierzwicki.moments.model.tracking.Tracking;
import pl.maciejnierzwicki.moments.model.user.User;

@Repository
public interface TrackingRepository extends CrudRepository<Tracking, Tracking> {

	/** Returns users following given user. */
	List<Tracking> getAllByFollowed(User user);
	/** Returns number of users following given user. */
	Long countAllByFollowed(User user);
	/** Returns users followed by given user. */
	List<Tracking> getAllByUser(User user);
	/** Returns number of users followed by given user. */
	Long countAllByUser(User user);
	/** Returns (if any) Tracking entity representing following of user given in second argument by user given in first argument. Otherwise null. */
	Tracking getByUserAndFollowed(User follower, User followed);
	
}