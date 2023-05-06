package pl.maciejnierzwicki.moments.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import pl.maciejnierzwicki.moments.converters.UserToProfileConverter;
import pl.maciejnierzwicki.moments.model.notification.Notification;
import pl.maciejnierzwicki.moments.model.notification.NotificationType;
import pl.maciejnierzwicki.moments.model.profile.Profile;
import pl.maciejnierzwicki.moments.model.tracking.Tracking;
import pl.maciejnierzwicki.moments.model.tracking.TrackingDTO;
import pl.maciejnierzwicki.moments.model.user.User;
import pl.maciejnierzwicki.moments.service.NotificationService;
import pl.maciejnierzwicki.moments.service.TrackingService;
import pl.maciejnierzwicki.moments.service.UserService;

@RestController
@RequestMapping("/users")
@CrossOrigin
@Slf4j
public class UserController {
	
	@Autowired
	private UserService userService;
	@Autowired
	private UserToProfileConverter userToProfileConverter;
	@Autowired
	private TrackingService trackingService;
	@Autowired
	private NotificationService notificationService;
	@Autowired
	private ModelMapper modelMapper;
	
	@GetMapping
	public ResponseEntity<List<Profile>> getAllUsers() {
		return new ResponseEntity<>(userService.getAll().stream().map(user -> userToProfileConverter.getProfile(user)).collect(Collectors.toList()), HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Profile> getUserProfileById(@PathVariable("id") Long userID) {
		User user = userService.getById(userID);
		if(user == null) { return new ResponseEntity<>((Profile) null, HttpStatus.NOT_FOUND); }
		Profile profile = userToProfileConverter.getProfile(user, true);
		return new ResponseEntity<>(profile, HttpStatus.OK);
	}
	
	@GetMapping("/{id}/tracking")
	public ResponseEntity<TrackingDTO> getTrackingOfUser(@AuthenticationPrincipal User user, @PathVariable("id") Long userID) {
		User trackedUser = userService.getById(userID);
		if(user == null) { return new ResponseEntity<>((TrackingDTO) null, HttpStatus.NOT_FOUND); }
		Tracking tracking = trackingService.getTrackingOf(user, trackedUser);
		TrackingDTO trackingDto = null;
		if(tracking != null) trackingDto = modelMapper.map(tracking, TrackingDTO.class);
		return new ResponseEntity<>(trackingDto, HttpStatus.OK);
	}
	
	
	@PostMapping("/{id}/follow")
	public ResponseEntity<HttpStatus> followUser(@AuthenticationPrincipal User user, @PathVariable("id") Long selectedUserID) {
		User selectedUser = userService.getById(selectedUserID);
		if(selectedUser == null) { return new ResponseEntity<>(HttpStatus.NOT_FOUND); }
		if(selectedUser.getId().equals(user.getId())) {
			return new ResponseEntity<>(HttpStatus.OK);
		}
		Tracking tracking = trackingService.getTrackingOf(user, selectedUser);
		if(tracking != null) {
			notificationService.getAllByTracking(tracking).forEach(notification -> notificationService.delete(notification));
			trackingService.delete(tracking);
			log.info("USER " + tracking.getUser().getUsername() + " unfollowed " + tracking.getFollowed().getUsername());
		}
		else {
			tracking = new Tracking();
			tracking.setFollowed(selectedUser);
			tracking.setUser(user);
			log.info("USER " + tracking.getUser().getUsername() + " now follows " + tracking.getFollowed().getUsername());
			trackingService.save(tracking);
			
			// prepare and save notification
			Notification notification = new Notification();
			notification.setUser(tracking.getFollowed());
			notification.setNotificationType(NotificationType.USER_FOLLOW);
			notification.setTracking(tracking);
			notificationService.save(notification);
			
		}
		
		return new ResponseEntity<>(HttpStatus.OK);
	}
	

}
