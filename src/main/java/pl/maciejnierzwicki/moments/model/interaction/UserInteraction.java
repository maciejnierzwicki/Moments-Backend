package pl.maciejnierzwicki.moments.model.interaction;

import javax.persistence.JoinColumn;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;

import lombok.Data;
import lombok.EqualsAndHashCode;
import pl.maciejnierzwicki.moments.model.user.User;

@Data
@EqualsAndHashCode(callSuper = false)
@MappedSuperclass
public class UserInteraction extends Interaction {
	
	@OneToOne
	@JoinColumn(name = "user_id", referencedColumnName = "id") 
	private User user;

}
