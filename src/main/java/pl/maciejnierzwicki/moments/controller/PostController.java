package pl.maciejnierzwicki.moments.controller;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;
import pl.maciejnierzwicki.moments.converters.PostToPostViewConverter;
import pl.maciejnierzwicki.moments.discord.DiscordBot;
import pl.maciejnierzwicki.moments.form.NewPostForm;
import pl.maciejnierzwicki.moments.model.comment.Comment;
import pl.maciejnierzwicki.moments.model.comment.CommentDTO;
import pl.maciejnierzwicki.moments.model.editpostform.EditPostForm;
import pl.maciejnierzwicki.moments.model.like.Like;
import pl.maciejnierzwicki.moments.model.like.LikeDTO;
import pl.maciejnierzwicki.moments.model.notification.Notification;
import pl.maciejnierzwicki.moments.model.notification.NotificationType;
import pl.maciejnierzwicki.moments.model.post.Post;
import pl.maciejnierzwicki.moments.model.postview.PostView;
import pl.maciejnierzwicki.moments.model.user.User;
import pl.maciejnierzwicki.moments.service.CommentService;
import pl.maciejnierzwicki.moments.service.LikeService;
import pl.maciejnierzwicki.moments.service.NotificationService;
import pl.maciejnierzwicki.moments.service.PostService;
import pl.maciejnierzwicki.moments.service.UserService;
import pl.maciejnierzwicki.moments.utils.FileUploadUtil;

@RestController
@RequestMapping("/posts")
@CrossOrigin(origins = { "127.0.0.1", "192.168.1.114", "localhost" })
@Slf4j
public class PostController {
	
	@Autowired
	private PostService postService;
	@Autowired
	private PostToPostViewConverter postToPostViewConverter;
	@Autowired
	private LikeService likeService;
	@Autowired
	private UserService userService;
	@Autowired
	private CommentService commentService;
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private ServletContext context;
	@Autowired
	private NotificationService notificationService;
	@Autowired
	private DiscordBot discordBot;
	
	private static final String UPLOAD_DIR = "/posts-content";
	
	@GetMapping
	public ResponseEntity<List<PostView>> getAllPosts() {
		return new ResponseEntity<>(postService.getAllFromNewest().stream().map(post -> postToPostViewConverter.getPostView(post)).collect(Collectors.toList()), HttpStatus.OK);
	}
	
	@GetMapping("/userid={id}")
	public ResponseEntity<List<PostView>> getAllUserPosts(@PathVariable("id") Long userId) {
		log.info("Looking for user with id " + userId);
		User user = userService.getById(userId);
		if(user != null) {
			log.info("user found.");
			return new ResponseEntity<>(postService.getAllByUserFromNewest(user).stream().map(post -> postToPostViewConverter.getPostView(post)).collect(Collectors.toList()), HttpStatus.OK);
		}
		throw new ResourceNotFoundException("User with id " + userId + " not found.");
	}
	
	@GetMapping("/userid={id}/page={page}")
	public ResponseEntity<List<PostView>> getUserPostsPage(@PathVariable("id") Long userId, @PathVariable("page") Integer page) {
		User user = userService.getById(userId);
		if(user != null) {
			return new ResponseEntity<>(postService.getByUserAndPage(user, page).stream().map(post -> postToPostViewConverter.getPostView(post)).collect(Collectors.toList()), HttpStatus.OK);
		}
		return new ResponseEntity<>((List<PostView>) null, HttpStatus.NOT_FOUND);
	}
	
	@GetMapping("/{id}/likes")
	public ResponseEntity<List<LikeDTO>> getAllPostLikes(@PathVariable("id") Long postId) {
		Post post = postService.getById(postId);
		if(post == null) {
			throw new ResourceNotFoundException("Post with id " + postId + " not found.");
		}
		return new ResponseEntity<>(likeService.getAllFromNewest(post).stream().map(like -> modelMapper.map(like, LikeDTO.class)).collect(Collectors.toList()), HttpStatus.OK);
	}
	
	@GetMapping("/{id}/likes/page={page}")
	public ResponseEntity<List<LikeDTO>> getLikesPage(@PathVariable("id") Long postId, @PathVariable("page") Integer page) {
		Post post = postService.getById(postId);
		if(post == null) {
			throw new ResourceNotFoundException("Post with id " + postId + " not found.");
		}
		return new ResponseEntity<>(likeService.getPage(post, page).stream().map(like -> modelMapper.map(like, LikeDTO.class)).collect(Collectors.toList()), HttpStatus.OK);
	}
	
	@GetMapping("/{id}/comments")
	public ResponseEntity<List<CommentDTO>> getAllPostComments(@PathVariable("id") Long postId) {
		Post post = postService.getById(postId);
		if(post == null) {
			throw new ResourceNotFoundException("Post with id " + postId + " not found.");
		}
		return new ResponseEntity<>(commentService.getAllFromNewest(post).stream().map(comment -> modelMapper.map(comment, CommentDTO.class)).collect(Collectors.toList()), HttpStatus.OK);
	}
	
	@GetMapping("/{id}/comments/page={page}")
	public ResponseEntity<List<CommentDTO>> getCommentsPage(@PathVariable("id") Long postId, @PathVariable("page") Integer page) {
		Post post = postService.getById(postId);
		if(post == null) {
			throw new ResourceNotFoundException("Post with id " + postId + " not found.");
		}
		return new ResponseEntity<>(commentService.getPage(post, page).stream().map(comment -> modelMapper.map(comment, CommentDTO.class)).collect(Collectors.toList()), HttpStatus.OK);
	}
	
	
	@GetMapping("/page={page}")
	public ResponseEntity<List<PostView>> getPosts(@PathVariable("page") Integer page) {
		return new ResponseEntity<>(postService.getPage(page).stream().map(post -> postToPostViewConverter.getPostView(post)).collect(Collectors.toList()), HttpStatus.OK);
	}
	
	
	@GetMapping("/{id}")
	public ResponseEntity<PostView> getPostById(@PathVariable("id") Long postID) {
		Post post = postService.getById(postID);
		if(post == null) { throw new ResourceNotFoundException("Post with id " + postID + " not found."); }
		File result=new File(post.getImageLocation());
		log.debug("File exists: " + result.exists());
		return new ResponseEntity<>(postToPostViewConverter.getPostView(post), HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<HttpStatus> deletePostById(@AuthenticationPrincipal User user, @PathVariable("id") Long postID) {
		Post post = postService.getById(postID);
		if(post == null) { return ResponseEntity.notFound().build(); }
		if(!post.getUser().getId().equals(user.getId())) { return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); }
		log.debug("Deleting notifications");
		notificationService.getAllByPost(post).forEach(notification -> notificationService.delete(notification));
		log.debug("Deleting likes");
		likeService.getAllByPost(post).forEach(like -> likeService.delete(like));
		log.debug("Deleting comments");
		commentService.getAllByPost(post).forEach(comment -> commentService.delete(comment));
		String picture = post.getImageLocation();
		String fullDeletePath = new File(".").getAbsolutePath() + picture;
		log.debug("Full delete path: " + fullDeletePath);
		if(picture != null && !(new File(fullDeletePath).delete())) {
			log.error("Error deleting file at path: " + fullDeletePath);
		}
		log.debug("Deleting post");
		postService.delete(post);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@PatchMapping("/{id}")
	public ResponseEntity<HttpStatus> editPostById(@AuthenticationPrincipal User user, @PathVariable("id") Long postID, @RequestBody EditPostForm editPostForm) {
		Post post = postService.getById(postID);
		if(post == null) { return ResponseEntity.notFound().build(); }
		if(!post.getUser().getId().equals(user.getId())) { return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); }
		post.setDescription(editPostForm.getDescription());
		postService.save(post);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	/**
	 * Stores new like or deletes existing like of given post from user.
	 * @Param user
	 * @Param postID
	 * @return
	 */
	@PostMapping("/{id}/like")
	public ResponseEntity<Void> likePost(@AuthenticationPrincipal User user, @PathVariable("id") Long postID) {
		Post post = postService.getById(postID);
		if(post == null) { throw new ResourceNotFoundException("Post with id " + postID + " not found."); }
		Like like = likeService.getByPostAndUser(post, user);
		List<Like> currentLikes;
		Set<Like> newLikes = new HashSet<>();
		if(like != null) { 
			currentLikes = likeService.getAllByPost(post);
			currentLikes.remove(like);
			newLikes.addAll(currentLikes);
			post.setLikes(newLikes);
			postService.save(post);
			notificationService.deleteByLike(like);
			likeService.delete(like);
			return ResponseEntity.ok().build(); 
		}
		like = new Like();
		like.setUser(user);
		like.setPost(post);
		
		like = likeService.save(like);
		currentLikes = likeService.getAllByPost(post);
		currentLikes.add(like);
		newLikes.addAll(currentLikes);
		post.setLikes(newLikes);
		post = postService.save(post); /** <-- **/
		
		// prepare and save notification
		// don't send notification if user interacts with its own content
		boolean isUserAuthor = post.getUser().getId().equals(user.getId());
		if(!isUserAuthor) {
			Notification notification = new Notification();
			notification.setUser(post.getUser());
			notification.setNotificationType(NotificationType.POST_LIKE);
			notification.setLike(like);
			notification.setPost(post);
			notificationService.save(notification);
		}
		

		return ResponseEntity.ok().build();
	}
	
	@PostMapping("/{id}/comment")
	public ResponseEntity<PostView> likePost(@AuthenticationPrincipal User user, @PathVariable("id") Long postID, @RequestBody String message) {
		Post post = postService.getById(postID);
		if(post == null) { throw new ResourceNotFoundException("Post with id " + postID + " not found."); }
		Comment comment = new Comment();
		comment.setMessage(message);
		comment.setUser(user);
		comment.setPost(post);
		
		comment = commentService.save(comment);
		List<Comment> currentComments = commentService.getAllByPost(post);
		Set<Comment> newComments = new HashSet<>();
		newComments.addAll(currentComments);
		newComments.add(comment);
		post.setComments(newComments);
		post = postService.save(post);
		
		// prepare and save notification
		// don't send notification if user interacts with its own content
		boolean isUserAuthor = post.getUser().getId().equals(user.getId());
		if(!isUserAuthor) {
			Notification notification = new Notification();
			notification.setUser(post.getUser());
			notification.setNotificationType(NotificationType.POST_COMMENT);
			notification.setComment(comment);
			notification.setPost(post);
			notificationService.save(notification);
		}
		

		return new ResponseEntity<>(postToPostViewConverter.getPostView(post), HttpStatus.OK);
	}
	
	@PostMapping(value = "/new")
	public ResponseEntity<PostView> upload(HttpServletRequest request, NewPostForm form, @RequestParam("image") MultipartFile image) throws IOException {
		String fileName = StringUtils.cleanPath(image.getOriginalFilename());
		
		Post post = new Post();
		post.setDescription(form.getDescription());
		
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User user = (User) authentication.getPrincipal();
		
		post.setUser(user);
		post.setImageLocation("");
		post = postService.save(post);
		String imageUrl = UPLOAD_DIR + "/" + post.getId() + "/" + fileName;
		FileUploadUtil.saveFile(new FileSystemResource("").getFile().getAbsolutePath() + UPLOAD_DIR + "/" + post.getId(), fileName, image);
		post.setImageLocation(imageUrl);
		post = postService.save(post);
		
		discordBot.sendPostMessage(post);
		return new ResponseEntity<>(postToPostViewConverter.getPostView(post), HttpStatus.OK);
	}
}
