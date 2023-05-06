package pl.maciejnierzwicki.moments.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import pl.maciejnierzwicki.moments.model.post.Post;
import pl.maciejnierzwicki.moments.model.postview.PostView;
import pl.maciejnierzwicki.moments.service.CommentService;
import pl.maciejnierzwicki.moments.service.LikeService;

@Service
public class PostToPostViewConverter {
	
	@Autowired
	private UserToProfileConverter profileService;
	@Autowired
	private LikeService likeService;
	@Autowired
	private CommentService commentService;
	
	@Cacheable(cacheNames = "postviews")
	public PostView getPostView(Post post) {
		PostView postView = new PostView();
		postView.setId(post.getId());
		postView.setDescription(post.getDescription());
		postView.setImageLocation(post.getImageLocation());
		postView.setCreationDate(post.getCreationDate());
		postView.setLikesCount(likeService.countAllByPost(post));
		postView.setCommentsCount(commentService.countAllByPost(post));
		postView.setAuthor(profileService.getProfile(post.getUser()));
		return postView;
	}

}
