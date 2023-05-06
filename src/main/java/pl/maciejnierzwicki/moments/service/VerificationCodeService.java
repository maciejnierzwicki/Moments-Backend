package pl.maciejnierzwicki.moments.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.maciejnierzwicki.moments.model.user.User;
import pl.maciejnierzwicki.moments.model.verificationcode.VerificationCode;
import pl.maciejnierzwicki.moments.repository.VerificationCodesRepository;

@Service
public class VerificationCodeService {
	
	@Autowired
	private VerificationCodesRepository verificationCodesRepo;
	
	public VerificationCode getByUser(User user) {
		return verificationCodesRepo.getByUser(user);
	}
	
	public VerificationCode getByCode(Integer code) {
		return verificationCodesRepo.getByCode(code);
	}
	
	public VerificationCode save(VerificationCode verificationCode) {
		return verificationCodesRepo.save(verificationCode);
	}
	
	public void delete(VerificationCode verificationCode) {
		verificationCodesRepo.delete(verificationCode);
	}

}
