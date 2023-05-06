package pl.maciejnierzwicki.moments.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import pl.maciejnierzwicki.moments.model.profile.Profile;
import pl.maciejnierzwicki.moments.model.profile.ProfileDetails;
import pl.maciejnierzwicki.moments.model.user.User;
import pl.maciejnierzwicki.moments.service.PostService;
import pl.maciejnierzwicki.moments.service.TrackingService;

@Service
public class UserToProfileConverter {
	
	@Autowired
	private TrackingService trackingService;
	@Autowired
	private PostService postService;
	
	public Profile getProfile(User user) {
		return getProfile(user, false);
	}
	
	@Cacheable(cacheNames = "profiles")
	public Profile getProfile(@NonNull User user, boolean withDetails) {
		Profile profile = new Profile();
		profile.setId(user.getId());
		profile.setUsername(user.getUsername());
		profile.setProfilePicture(user.getProfilePicture());
		profile.setVerified(user.getVerified());
		if(withDetails) {
			ProfileDetails details = new ProfileDetails();
			details.setEmail(user.getEmail());
			details.setRegistrationDate(user.getRegistrationDate());
			details.setLastOnlineDate(user.getLastOnlineDate());
			details.setFollowersCount(trackingService.getFollowersCount(user));
			details.setFollowingCount(trackingService.getFollowedCount(user));
			details.setPostsCount(postService.getPostsCount(user));
			profile.setProfileDetails(details);
		}
		return profile;
	}

}
