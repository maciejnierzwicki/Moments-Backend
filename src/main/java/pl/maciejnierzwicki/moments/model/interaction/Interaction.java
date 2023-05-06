package pl.maciejnierzwicki.moments.model.interaction;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;

import lombok.Data;

@Data
@MappedSuperclass
class Interaction {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected Long id;
	
	@Column(name = "sent_date")
	private Date sentDate;
	
	@PrePersist
	private void setSentDate() {
		this.sentDate = new Date();
	}

}
