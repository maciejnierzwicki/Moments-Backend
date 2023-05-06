package pl.maciejnierzwicki.moments.model.profile;

import java.util.Date;

import lombok.Data;

@Data
public class ProfileDetails {
	
	private String email;
	private Date registrationDate;
	private Date lastOnlineDate;
	private Long followersCount;
	private Long followingCount;
	private Long postsCount;

}
