package pl.maciejnierzwicki.moments.model.tracking;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;
import pl.maciejnierzwicki.moments.model.interaction.UserInteraction;
import pl.maciejnierzwicki.moments.model.user.User;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "Tracking")
public class Tracking extends UserInteraction {

	@ManyToOne
	@JoinColumn(name = "followed_id", referencedColumnName = "id") 
	private User followed;


}
