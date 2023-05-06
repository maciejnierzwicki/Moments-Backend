package pl.maciejnierzwicki.moments.model.user;

import java.util.Date;

import lombok.Data;

@Data
public class UserDTO  {
	
	private Long id;
	private Boolean active;
	private String username;
	private String password;
	private String email;
	private Date registrationDate;
	private Date lastOnlineDate;
	private Long followersCount;
	private Long followingCount;
	private String profilePicture;

}
