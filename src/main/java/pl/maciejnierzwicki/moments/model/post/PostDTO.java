package pl.maciejnierzwicki.moments.model.post;

import java.util.Date;

import lombok.Data;
import pl.maciejnierzwicki.moments.model.user.UserDTO;

@Data
public class PostDTO {
	
	private Long id;
	private UserDTO user;
	private Date creationDate;
	private String description;
	private String imageLocation;
	private Long likesCount;
	private Long commentsCount;

}
