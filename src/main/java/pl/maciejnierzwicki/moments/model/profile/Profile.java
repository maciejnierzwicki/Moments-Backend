package pl.maciejnierzwicki.moments.model.profile;

import lombok.Data;

@Data
public class Profile {
	
	private Long id;
	private String username;
	private String profilePicture;
	private ProfileDetails profileDetails;
	private Boolean verified;
}
