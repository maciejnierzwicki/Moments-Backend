package pl.maciejnierzwicki.moments.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import pl.maciejnierzwicki.moments.model.passwordresetcode.PasswordResetCode;
import pl.maciejnierzwicki.moments.model.user.User;


@Repository
public interface PasswordResetCodesRepository extends CrudRepository<PasswordResetCode, Long>{
	List<PasswordResetCode> findAll();
	PasswordResetCode getByUser(User user);
	PasswordResetCode getByCode(Integer code);
}
