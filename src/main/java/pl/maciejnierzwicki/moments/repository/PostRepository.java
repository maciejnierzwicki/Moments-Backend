package pl.maciejnierzwicki.moments.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import pl.maciejnierzwicki.moments.model.post.Post;
import pl.maciejnierzwicki.moments.model.user.User;

@Repository
public interface PostRepository extends CrudRepository<Post, Long> {
	
	List<Post> findByOrderByCreationDateDesc();
	List<Post> findAll();
	List<Post> getAllByUser(User user);
	Long countAllByUser(User user);
	List<Post> findByUserOrderByCreationDateDesc(User user, Pageable pageable);
	List<Post> findByOrderByCreationDateDesc(Pageable pageable);

}
