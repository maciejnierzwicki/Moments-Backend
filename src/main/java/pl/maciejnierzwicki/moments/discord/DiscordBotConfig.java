package pl.maciejnierzwicki.moments.discord;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Configuration
@Data
public class DiscordBotConfig {
	
	@Value("${moments.discord.botToken}")
	private String botToken;
	
	@Value("${moments.discord.channelID}")
	private String channelID;
	
	@Value("${moments.discord.postReferenceLink}")
	private String postReferenceLink;
	
	@Value("${moments.discord.defaultAvatarLink}")
	private String defaultAvatarLink;

}
