package pl.maciejnierzwicki.moments.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import pl.maciejnierzwicki.moments.model.user.User;
import pl.maciejnierzwicki.moments.repository.UserRepository;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepo;
	
	@Cacheable(cacheNames = "users")
	public User getByUsername(String username) {
		return userRepo.getByUsername(username);
	}
	
	@Cacheable(cacheNames = "users")
	public User getByEmail(String email) {
		return userRepo.getByEmail(email);
	}
	
	@Cacheable(cacheNames = "users")
	public User getById(Long id) {
		return userRepo.findById(id).orElse(null);
	}
	
	@Cacheable(cacheNames = "users")
	public List<User> getAll() {
		return userRepo.findAll();
	}
	
	@CacheEvict(cacheNames = { "users", "posts" }, allEntries = true)
	public User save(User user) {
		return userRepo.save(user);
	}
	
	@CacheEvict(cacheNames = { "users", "posts" }, allEntries = true)
	public void delete(User user) {
		userRepo.delete(user);
	}

}
