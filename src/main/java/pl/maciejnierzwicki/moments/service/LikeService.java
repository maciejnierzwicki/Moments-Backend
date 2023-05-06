package pl.maciejnierzwicki.moments.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import pl.maciejnierzwicki.moments.model.like.Like;
import pl.maciejnierzwicki.moments.model.post.Post;
import pl.maciejnierzwicki.moments.model.user.User;
import pl.maciejnierzwicki.moments.repository.LikeRepository;

@Service
public class LikeService {
	
	@Autowired
	private	LikeRepository likeRepo;
	
	@Cacheable(cacheNames = "likes")
	public List<Like> getAllByPost(Post post) {
		return likeRepo.getAllByPost(post);
	}
	
	@Cacheable(cacheNames = "likeCounts")
	public Long countAllByPost(Post post) {
		return likeRepo.countAllByPost(post);
	}
	
	@Cacheable(cacheNames = "likes")
	public List<Like> getAllByUser(User user) {
		return likeRepo.getAllByUser(user);
	}
	
	@Cacheable(cacheNames = "likes")
	public Like getByPostAndUser(Post post, User user) {
		return likeRepo.getByPostAndUser(post, user);
	}
	
	@Cacheable(cacheNames = "likes")
	public Like getById(Long id) {
		return likeRepo.findById(id).orElse(null);
	}
	
	@Cacheable(cacheNames = "likes")
	public List<Like> getAll() {
		return likeRepo.findAll();
	}
	
	@Cacheable(cacheNames = "likes")
	public List<Like> getAllFromNewest(Post post) {
		return likeRepo.findByPostOrderBySentDateDesc(post);
	}
	
	@Cacheable(cacheNames = "likes")
	public List<Like> getPage(Post post, int page) {
		return likeRepo.findByPostOrderBySentDateDesc(post, PageRequest.of(page, 10));
	}
	
	@CacheEvict(cacheNames = { "likes", "likeCounts" }, allEntries = true)
	public Like save(Like like) {
		return likeRepo.save(like);
	}
	
	@CacheEvict(cacheNames = { "likes", "likeCounts" }, allEntries = true)
	public void delete(Like like) {
		likeRepo.delete(like);
	}

}
