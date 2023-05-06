package pl.maciejnierzwicki.moments.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import pl.maciejnierzwicki.moments.model.like.Like;
import pl.maciejnierzwicki.moments.model.notification.Notification;
import pl.maciejnierzwicki.moments.model.post.Post;
import pl.maciejnierzwicki.moments.model.tracking.Tracking;
import pl.maciejnierzwicki.moments.model.user.User;
import pl.maciejnierzwicki.moments.repository.NotificationsRepository;

@Service
public class NotificationService {
	
	@Autowired
	private	NotificationsRepository notificationsRepo;
	
	
	@Cacheable(cacheNames = "notifications")
	public List<Notification> getAllByUser(User user) {
		return notificationsRepo.getAllByUser(user);
	}
	
	@Cacheable(cacheNames = "notifications")
	public List<Notification> getAllByPost(Post post) {
		return notificationsRepo.getAllByPost(post);
	}
	
	@Cacheable(cacheNames = "notifications")
	public List<Notification> getAllByTracking(Tracking tracking) {
		return notificationsRepo.getAllByTracking(tracking);
	}
	
	@Cacheable(cacheNames = "notifications")
	public Notification getByLike(Like like) {
		return notificationsRepo.getByLike(like);
	}
	
	public Notification getById(Long id) {
		return notificationsRepo.getById(id);
	}
	
	@Cacheable(cacheNames = "notificationCounts")
	public Long countAllByUser(User user) {
		return notificationsRepo.countAllByUser(user);
	}
	
	@Cacheable(cacheNames = "notifications")
	public List<Notification> getAllUnreadByUser(User user) {
		return notificationsRepo.getAllByUser(user);
	}
	
	@Cacheable(cacheNames = "notificationCounts")
	public Long countAllUnreadByUser(User user) {
		return notificationsRepo.countByUserAndRead(user, false);
	}
	
	@Cacheable(cacheNames = "notifications")
	public List<Notification> getAll() {
		return notificationsRepo.findAll();
	}
	
	@Cacheable(cacheNames = "notifications")
	public List<Notification> getAllOfUserFromNewest(User user) {
		return notificationsRepo.findByUserOrderBySentDateDesc(user);
	}
	
	@Cacheable(cacheNames = "notifications")
	public List<Notification> getPageOfUser(User user, int page) {
		return notificationsRepo.findByUserOrderBySentDateDesc(user, PageRequest.of(page, 10));
	}
	
	@CacheEvict(cacheNames = { "notifications", "notificationCounts" }, allEntries = true)
	public Notification save(Notification notification) {
		return notificationsRepo.save(notification);
	}
	
	@CacheEvict(cacheNames = { "notifications", "notificationCounts" }, allEntries = true)
	public void delete(Notification notification) {
		notificationsRepo.delete(notification);
	}
	
	@CacheEvict(cacheNames = { "notifications", "notificationCounts" }, allEntries = true)
	public void deleteByLike(Like like) {
		notificationsRepo.deleteByLike(like);
	}

}
