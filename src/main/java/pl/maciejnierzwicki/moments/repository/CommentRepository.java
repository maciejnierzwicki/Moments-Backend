package pl.maciejnierzwicki.moments.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import pl.maciejnierzwicki.moments.model.comment.Comment;
import pl.maciejnierzwicki.moments.model.post.Post;
import pl.maciejnierzwicki.moments.model.user.User;

@Repository
public interface CommentRepository extends CrudRepository<Comment, Long> {

	List<Comment> getAllByPost(Post post);
	Long countAllByPost(Post post);
	List<Comment> getAllByUser(User user);
	List<Comment> findByPostOrderBySentDateDesc(Post post);
	List<Comment> findByPostOrderBySentDateDesc(Post post, Pageable pageable);
	List<Comment> findByOrderBySentDateDesc();
	List<Comment> findByOrderBySentDateDesc(Pageable pageable);
}
