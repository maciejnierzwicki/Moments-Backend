package pl.maciejnierzwicki.moments.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import pl.maciejnierzwicki.moments.model.Role;
import pl.maciejnierzwicki.moments.repository.RoleRepository;


@Service
public class RoleService {
	
	@Autowired
	private RoleRepository roleRepo;
	
	@Cacheable(cacheNames = "roles")
	public Role getById(String id) {
		return roleRepo.findById(id).orElse(null);
	}
	
	@Cacheable(cacheNames = "roles")
	public List<Role> getAll() {
		return roleRepo.findAll();
	}
	
	@CacheEvict(cacheNames = "roles", allEntries = true)
	public Role save(Role role) {
		return roleRepo.save(role);
	}
	
	@CacheEvict(cacheNames = "roles", allEntries = true)
	public void delete(Role role) {
		roleRepo.delete(role);
	}

}
