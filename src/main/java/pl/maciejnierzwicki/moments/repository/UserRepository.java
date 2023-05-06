package pl.maciejnierzwicki.moments.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import pl.maciejnierzwicki.moments.model.user.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
	
	List<User> findAll();
	User getByUsername(String username);
	User getByEmail(String email);
	User getById(Long id);

}