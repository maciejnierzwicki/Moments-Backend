package pl.maciejnierzwicki.moments.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import pl.maciejnierzwicki.moments.model.like.Like;
import pl.maciejnierzwicki.moments.model.notification.Notification;
import pl.maciejnierzwicki.moments.model.post.Post;
import pl.maciejnierzwicki.moments.model.tracking.Tracking;
import pl.maciejnierzwicki.moments.model.user.User;

@Repository
public interface NotificationsRepository extends CrudRepository<Notification, Integer> {

	List<Notification> findAll();
	List<Notification> getAllByUser(User user);
	List<Notification> getAllByPost(Post post);
	List<Notification> getAllByTracking(Tracking tracking);
	Notification getById(Long id);
	Long countAllByUser(User user);
	List<Notification> getAllByUserAndRead(User user, Boolean read);
	Long countByUserAndRead(User user, Boolean read);
	List<Notification> findByUserOrderBySentDateDesc(User user);
	List<Notification> findByUserAndReadOrderBySentDateDesc(User user, Boolean read);
	List<Notification> findByUserOrderBySentDateDesc(User user, Pageable pageable);
	List<Notification> findByUserAndReadOrderBySentDateDesc(User user, Boolean read, Pageable pageable);
	Notification getByLike(Like like);
	void deleteByLike(Like like);
}
