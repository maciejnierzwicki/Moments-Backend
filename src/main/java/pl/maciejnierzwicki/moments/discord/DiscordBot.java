package pl.maciejnierzwicki.moments.discord;

import java.awt.Color;
import java.io.File;
import java.util.Optional;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import pl.maciejnierzwicki.moments.model.post.Post;
import pl.maciejnierzwicki.moments.model.user.User;

@Slf4j
@Component
public class DiscordBot {

	private DiscordApi api;
	private TextChannel textChannel;
	
	private DiscordBotConfig config;
	
	@Value("${moments.serviceBasePath}")
	private String basePath;
	
	public DiscordBot(@Autowired DiscordBotConfig botConfig) {
		this.config = botConfig;
		this.api = new DiscordApiBuilder().setToken(config.getBotToken()).login().join();
		String channelID = config.getChannelID();
		Optional<TextChannel> opTextChannel = this.api.getTextChannelById(channelID);
		if(opTextChannel.isPresent()) {
			this.textChannel = opTextChannel.get();
		}
		else {
			log.error("Discord bot cannot connect to configured channel.");
		}
		
	}
	
	public DiscordApi getDiscordApi() {
		return this.api;
	}
	
	public void disconnect() {
		this.api.disconnect();
	}
	
	public void sendPostMessage(Post post) {
		User user = post.getUser();
		File file = new File(new File(".").getAbsolutePath() + post.getImageLocation());
		
		String profilePic = user.getProfilePicture();
		String profilePicPath;
		if(profilePic == null || profilePic.isEmpty()) {
			profilePicPath = config.getDefaultAvatarLink();
		}
		else {
			profilePicPath = basePath + profilePic;
		}
		String link = config.getPostReferenceLink().replace("{id}", String.valueOf(post.getId()));
		EmbedBuilder embed = new EmbedBuilder()
			    .setTitle("New post")
			    .setDescription(post.getDescription())
			    .setAuthor(user.getUsername(), "", profilePicPath)
			    .addField("Go to the post", link)
			    .setColor(Color.BLUE)
			    .setImage(file);
		this.textChannel.sendMessage(embed);
	}
	
}
