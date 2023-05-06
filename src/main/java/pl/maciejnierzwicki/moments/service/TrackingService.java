package pl.maciejnierzwicki.moments.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import pl.maciejnierzwicki.moments.model.tracking.Tracking;
import pl.maciejnierzwicki.moments.model.user.User;
import pl.maciejnierzwicki.moments.repository.TrackingRepository;

@Service
public class TrackingService {
	
	@Autowired
	private TrackingRepository trackingRepo;
	
	public List<Tracking> getAllFollowersOfUser(User user) {
		return trackingRepo.getAllByFollowed(user);
	}
	public List<Tracking> getAllFollowedByUser(User user) {
		return trackingRepo.getAllByUser(user);
	}
	
	public Tracking getTrackingOf(User follower, User followed) {
		return trackingRepo.getByUserAndFollowed(follower, followed);
	}
	public Long getFollowersCount(User followed) {
		return trackingRepo.countAllByFollowed(followed);
	}
	public Long getFollowedCount(User follower) {
		return trackingRepo.countAllByUser(follower);
	}
	@CacheEvict(cacheNames = { "profiles", "users" }, allEntries = true)
	public void save(Tracking tracking) {
		trackingRepo.save(tracking);
	}
	@CacheEvict(cacheNames = { "profiles", "users" }, allEntries = true)
	public void delete(Tracking tracking) {
		trackingRepo.delete(tracking);
	}

}
