package pl.maciejnierzwicki.moments.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import pl.maciejnierzwicki.moments.model.user.User;
import pl.maciejnierzwicki.moments.model.verificationcode.VerificationCode;


@Repository
public interface VerificationCodesRepository extends CrudRepository<VerificationCode, Long>{
	List<VerificationCode> findAll();
	VerificationCode getById(Long id);
	VerificationCode getByUser(User user);
	VerificationCode getByCode(Integer code);
}
