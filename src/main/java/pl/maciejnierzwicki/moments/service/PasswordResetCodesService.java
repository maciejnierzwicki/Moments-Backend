package pl.maciejnierzwicki.moments.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.maciejnierzwicki.moments.model.passwordresetcode.PasswordResetCode;
import pl.maciejnierzwicki.moments.model.user.User;
import pl.maciejnierzwicki.moments.repository.PasswordResetCodesRepository;

@Service
public class PasswordResetCodesService {
	
	@Autowired
	private PasswordResetCodesRepository passwordResetCodesRepo;
	
	public PasswordResetCode getByUser(User user) {
		return passwordResetCodesRepo.getByUser(user);
	}
	
	public PasswordResetCode getByCode(Integer code) {
		return passwordResetCodesRepo.getByCode(code);
	}
	
	public PasswordResetCode save(PasswordResetCode passwordResetCode) {
		return passwordResetCodesRepo.save(passwordResetCode);
	}
	
	public void delete(PasswordResetCode passwordResetCode) {
		passwordResetCodesRepo.delete(passwordResetCode);
	}

}
