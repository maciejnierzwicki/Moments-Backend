package pl.maciejnierzwicki.moments.model.comment;

import java.util.Date;

import lombok.Data;
import pl.maciejnierzwicki.moments.model.user.UserDTO;

@Data
public class CommentDTO {
	
	private Long id;
	private UserDTO user;
	private Date sentDate;
	private String message;

}
