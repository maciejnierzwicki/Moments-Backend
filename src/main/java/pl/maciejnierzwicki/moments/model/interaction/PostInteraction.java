package pl.maciejnierzwicki.moments.model.interaction;

import javax.persistence.CascadeType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import lombok.Data;
import lombok.EqualsAndHashCode;
import pl.maciejnierzwicki.moments.model.post.Post;

@Data
@EqualsAndHashCode(callSuper = false)
@MappedSuperclass
public class PostInteraction extends UserInteraction {
	
	@EqualsAndHashCode.Exclude
	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "post_id", referencedColumnName = "id") 
	private Post post;

}
