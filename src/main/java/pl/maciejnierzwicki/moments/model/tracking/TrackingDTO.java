package pl.maciejnierzwicki.moments.model.tracking;

import java.util.Date;

import lombok.Data;
import pl.maciejnierzwicki.moments.model.user.UserDTO;

@Data
public class TrackingDTO {
	
	private Long id;
	private UserDTO user;
	private UserDTO followed;
	private Date sentDate;

}
