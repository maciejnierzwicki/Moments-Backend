package pl.maciejnierzwicki.moments.model.post;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;
import pl.maciejnierzwicki.moments.model.comment.Comment;
import pl.maciejnierzwicki.moments.model.like.Like;
import pl.maciejnierzwicki.moments.model.user.User;

@Data
@Entity
@Table(name = "Posts")
public class Post {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	private User user;
	
	@Column(name = "creation_date")
	private Date creationDate;
	
	private String description = "";
	
	@Column(name = "image_location")
	private String imageLocation; // TODO: Change field name to something more descriptive; this field holds image url.
	
	@EqualsAndHashCode.Exclude
	@OneToMany(targetEntity = Like.class, mappedBy = "post")
	private Set<Like> likes = new HashSet<>();
	
	@OneToMany(targetEntity = Comment.class, mappedBy = "post")
	private Set<Comment> comments = new HashSet<>();
	
	
	@PrePersist
	void setUploadDate() {
		this.creationDate = new Date();
	}
}
