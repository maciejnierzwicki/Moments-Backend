package pl.maciejnierzwicki.moments.model.verificationcode;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import lombok.Data;
import pl.maciejnierzwicki.moments.model.user.User;

@Data
@Table(name = "VerificationCodes")
@Entity
public class VerificationCode {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private Integer code;
	
	@OneToOne
	@JoinColumn(name = "user_id")
	private User user;
	
	@Column(name = "creation_date")
	private Date creationDate;
	
	@PrePersist
	void setCreationDate() {
		this.creationDate = new Date();
	}

}
