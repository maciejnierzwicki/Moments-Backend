package pl.maciejnierzwicki.moments.config.security.jwt.refresh;

import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;
import pl.maciejnierzwicki.moments.model.user.User;

@Data
@Entity
@Table(name = "RefreshTokens")
public class RefreshToken {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@OneToOne(targetEntity = User.class)
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	private User user;
	private String token;
	@Column(name = "expiry_date")
	private Instant expiryDate;
	 
	public RefreshToken(User user, String token, Instant expiryDate) {
		this.user = user;
		this.token = token;
		this.expiryDate = expiryDate;
	}
	
	public RefreshToken() {}

}
