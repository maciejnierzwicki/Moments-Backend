package pl.maciejnierzwicki.moments.model.notification;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import lombok.Data;
import pl.maciejnierzwicki.moments.model.comment.Comment;
import pl.maciejnierzwicki.moments.model.like.Like;
import pl.maciejnierzwicki.moments.model.post.Post;
import pl.maciejnierzwicki.moments.model.tracking.Tracking;
import pl.maciejnierzwicki.moments.model.user.User;

@Data
@Entity
@Table(name = "Notifications")
public class Notification {
	
	@Id
	@GeneratedValue
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "user_id", referencedColumnName = "id") 
	private User user;
	
	@Column(name = "sent_date")
	private Date sentDate;
	
	@Column(name = "read")
	private Boolean read;
	
	@Column(name = "notification_type")
	private NotificationType notificationType;
	
	@OneToOne
	@JoinColumn(name = "post_id", referencedColumnName = "id")
	private Post post;
	
	@OneToOne
	@JoinColumn(name = "like_id", referencedColumnName = "id")
	private Like like;
	
	@OneToOne
	@JoinColumn(name = "comment_id", referencedColumnName = "id")
	private Comment comment;
	
	@OneToOne
	@JoinColumn(name = "tracking_id", referencedColumnName = "id")
	private Tracking tracking;
	
	@PrePersist
	private void setDefaults() {
		this.sentDate = new Date();
		this.read = false;
	}
	

}
