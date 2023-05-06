package pl.maciejnierzwicki.moments;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import pl.maciejnierzwicki.moments.model.user.User;
import pl.maciejnierzwicki.moments.service.UserService;
@Service
public class UserRepositoryUserDetailsService implements UserDetailsService {
	
	@Autowired
	private UserService userService;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userService.getByUsername(username);
		if(user != null) {
			return user;
		}
		throw new UsernameNotFoundException("User not found: '" + username);
	}
 }
