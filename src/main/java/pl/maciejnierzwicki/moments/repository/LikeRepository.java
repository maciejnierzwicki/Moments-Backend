package pl.maciejnierzwicki.moments.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import pl.maciejnierzwicki.moments.model.like.Like;
import pl.maciejnierzwicki.moments.model.post.Post;
import pl.maciejnierzwicki.moments.model.user.User;

@Repository
public interface LikeRepository extends CrudRepository<Like, Long> {
	
	List<Like> findAll();
	List<Like> getAllByPost(Post post);
	Long countAllByPost(Post post);
	List<Like> getAllByUser(User user);
	Like getByPostAndUser(Post post, User user);
	List<Like> findByPostOrderBySentDateDesc(Post post);
	List<Like> findByPostOrderBySentDateDesc(Post post, Pageable pageable);
	List<Like> findByOrderBySentDateDesc();
	List<Like> findByOrderBySentDateDesc(Pageable pageable);

}
