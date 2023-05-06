package pl.maciejnierzwicki.moments.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import pl.maciejnierzwicki.moments.model.post.Post;
import pl.maciejnierzwicki.moments.model.user.User;
import pl.maciejnierzwicki.moments.repository.PostRepository;

@Service
public class PostService {
	
	@Autowired
	private PostRepository postRepo;
	
	@Cacheable(cacheNames = "posts")
	public List<Post> getAllByUser(User user) {
		return postRepo.getAllByUser(user);
	}
	
	@Cacheable(cacheNames = "posts")
	public Long getPostsCount(User user) {
		return postRepo.countAllByUser(user);
	}
	
	@Cacheable(cacheNames = "posts")
	public Post getById(Long id) {
		return postRepo.findById(id).orElse(null);
	}
	
	@Cacheable(cacheNames = "posts")
	public List<Post> getAll() {
		return postRepo.findAll();
	}
	
	@Cacheable(cacheNames = "posts")
	public List<Post> getAllByUserFromNewest(User user) {
		return postRepo.findByUserOrderByCreationDateDesc(user, PageRequest.of(0, 3));
	}
	
	@Cacheable(cacheNames = "posts")
	public List<Post> getByUserAndPage(User user, int page) {
		return postRepo.findByUserOrderByCreationDateDesc(user, PageRequest.of(page, 10));
	}
	
	@Cacheable(cacheNames = "posts")
	public List<Post> getAllFromNewest() {
		return postRepo.findByOrderByCreationDateDesc();
	}
	
	@Cacheable(cacheNames = "posts")
	public List<Post> getPage(int page) {
		return postRepo.findByOrderByCreationDateDesc(PageRequest.of(page, 10));
	}
	
	@CacheEvict(cacheNames = { "posts", "postviews" }, allEntries = true)
	public Post save(Post post) {
		return postRepo.save(post);
	}
	
	@CacheEvict(cacheNames = { "posts", "postviews" }, allEntries = true)
	public void delete(Post post) {
		postRepo.delete(post);
	}

}
