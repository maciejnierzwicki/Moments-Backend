package pl.maciejnierzwicki.moments.model.notification;

import java.util.Date;

import lombok.Data;
import pl.maciejnierzwicki.moments.model.comment.CommentDTO;
import pl.maciejnierzwicki.moments.model.like.LikeDTO;
import pl.maciejnierzwicki.moments.model.post.PostDTO;
import pl.maciejnierzwicki.moments.model.tracking.TrackingDTO;
import pl.maciejnierzwicki.moments.model.user.UserDTO;

@Data
public class NotificationDTO {
	
	private Long id;
	private UserDTO user;
	private Date sentDate;
	private Boolean read;
	private NotificationType notificationType;
	private PostDTO post;
	private LikeDTO like;
	private CommentDTO comment;
	private TrackingDTO tracking;
	

}
