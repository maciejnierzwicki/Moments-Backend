package pl.maciejnierzwicki.moments.model.postview;

import java.util.Date;

import lombok.Data;
import pl.maciejnierzwicki.moments.model.profile.Profile;

@Data
public class PostView {
	
	private Long id;
	private Date creationDate;
	private String description;
	private String imageLocation;
	private Long likesCount;
	private Long commentsCount;
	private Profile author;

}
