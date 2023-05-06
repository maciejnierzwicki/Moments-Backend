package pl.maciejnierzwicki.moments.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import pl.maciejnierzwicki.moments.model.comment.Comment;
import pl.maciejnierzwicki.moments.model.post.Post;
import pl.maciejnierzwicki.moments.repository.CommentRepository;

@Service
public class CommentService {
	
	@Autowired
	private	CommentRepository commentRepo;
	
	@Cacheable(cacheNames = "comments")
	public List<Comment> getAllByPost(Post post) {
		return commentRepo.getAllByPost(post);
	}
	
	@Cacheable(cacheNames = "commentCounts")
	public Long countAllByPost(Post post) {
		return commentRepo.countAllByPost(post);
	}
	
	
	@Cacheable(cacheNames = "comments")
	public Iterable<Comment> getAll() {
		return commentRepo.findAll();
	}
	
	@Cacheable(cacheNames = "comments")
	public List<Comment> getAllFromNewest(Post post) {
		return commentRepo.findByPostOrderBySentDateDesc(post);
	}
	
	@Cacheable(cacheNames = "comments")
	public List<Comment> getPage(Post post, int page) {
		return commentRepo.findByPostOrderBySentDateDesc(post, PageRequest.of(page, 10));
	}
	
	@CacheEvict(cacheNames = { "comments", "commentCounts" }, allEntries = true)
	public Comment save(Comment comment) {
		return commentRepo.save(comment);
	}
	
	@CacheEvict(cacheNames = { "comments", "commentCounts" }, allEntries = true)
	public void delete(Comment comment) {
		commentRepo.delete(comment);
	}

}
