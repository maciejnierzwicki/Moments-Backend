package pl.maciejnierzwicki.moments.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import pl.maciejnierzwicki.moments.model.Role;


@Repository
public interface RoleRepository extends CrudRepository<Role, String>{
	List<Role> findAll();
	Role getById(String id);
}
