package pl.maciejnierzwicki.moments.model.comment;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;
import pl.maciejnierzwicki.moments.model.interaction.PostInteraction;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "Comments")
public class Comment extends PostInteraction {
	
	private String message;

}
