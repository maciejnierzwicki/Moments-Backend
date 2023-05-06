package pl.maciejnierzwicki.moments.model.like;

import java.util.Date;

import lombok.Data;
import pl.maciejnierzwicki.moments.model.user.UserDTO;

@Data
public class LikeDTO {
	
	private Long id;
	private UserDTO user;
	private Date sentDate;

}
