package pl.maciejnierzwicki.moments.utils;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class NumberUtils {
	
	private Random random;
	
	public NumberUtils() {
		 try {
			random = SecureRandom.getInstanceStrong();
		} catch (NoSuchAlgorithmException e) {
			log.error(e.getMessage());
		}
		
	}
	
	public Integer generate6DigitsCode() {
		StringBuilder builder = new StringBuilder();
		int digit;
		for(int i = 0; i < 6; i++) {
			digit = random.nextInt(10);
			builder.append(digit);
		}
		return Integer.parseInt(builder.toString());
	}

}
