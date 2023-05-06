package pl.maciejnierzwicki.moments.model.like;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;
import pl.maciejnierzwicki.moments.model.interaction.PostInteraction;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "Likes")
public class Like extends PostInteraction {}
