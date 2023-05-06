package pl.maciejnierzwicki.moments.controller;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;
import pl.maciejnierzwicki.moments.ValidationErrorsMessageBuilder;
import pl.maciejnierzwicki.moments.controller.error.EmailUsedException;
import pl.maciejnierzwicki.moments.controller.error.FormInvalidException;
import pl.maciejnierzwicki.moments.controller.error.UserExistsException;
import pl.maciejnierzwicki.moments.form.EditEmailForm;
import pl.maciejnierzwicki.moments.form.EditPasswordForm;
import pl.maciejnierzwicki.moments.form.EditPersonalDataForm;
import pl.maciejnierzwicki.moments.form.EditUsernameForm;
import pl.maciejnierzwicki.moments.form.ProfilePictureForm;
import pl.maciejnierzwicki.moments.mail.MailService;
import pl.maciejnierzwicki.moments.model.notification.Notification;
import pl.maciejnierzwicki.moments.model.notification.NotificationDTO;
import pl.maciejnierzwicki.moments.model.notification.NotificationsResponse;
import pl.maciejnierzwicki.moments.model.user.User;
import pl.maciejnierzwicki.moments.model.user.UserDTO;
import pl.maciejnierzwicki.moments.model.verificationcode.VerificationCode;
import pl.maciejnierzwicki.moments.model.verificationcodeconfirmform.VerificationCodeConfirmForm;
import pl.maciejnierzwicki.moments.service.NotificationService;
import pl.maciejnierzwicki.moments.service.UserService;
import pl.maciejnierzwicki.moments.service.VerificationCodeService;
import pl.maciejnierzwicki.moments.utils.FileUploadUtil;
import pl.maciejnierzwicki.moments.utils.NumberUtils;

@RestController
@RequestMapping("/account")
@CrossOrigin
@Slf4j
public class AccountController {
	
	@Autowired
	private UserService userService;
	@Autowired
	private NotificationService notificationService;
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private ValidationErrorsMessageBuilder validationErrorMessagesBuilder;
	@Autowired
	private PasswordEncoder encoder;
	@Autowired
	private VerificationCodeService verificationCodeService;
	@Autowired
	private MailService emailUtils;
	@Autowired
	private NumberUtils numberUtils;
	
	private static final String PROFILE_PIC_UPLOAD_DIR = "/profile-pictures";
	
	@PostMapping("/verification")
	public ResponseEntity<HttpStatus> processVerificationCodeRequest(@AuthenticationPrincipal User user) {
		Integer code = numberUtils.generate6DigitsCode();
		VerificationCode verificationCode = new VerificationCode();
		verificationCode.setCode(code);
		verificationCode.setUser(user);
		verificationCodeService.save(verificationCode);
		emailUtils.sendActivationMail(verificationCode);
		return ResponseEntity.ok().build();
	}

	
	@PostMapping("/verification/confirm")
	public ResponseEntity<HttpStatus> processVerificationCodeConfirmRequest(@AuthenticationPrincipal User user, @RequestBody @Valid VerificationCodeConfirmForm form, Errors errors) throws FormInvalidException {
		if(errors.hasErrors()) {
			throw new FormInvalidException(errors.getAllErrors().get(0).getDefaultMessage());		
		}
		VerificationCode verificationCode = verificationCodeService.getByCode(form.getVerificationCode());
		if(verificationCode == null || isExpired(verificationCode) || !verificationCode.getUser().getId().equals(user.getId())) return ResponseEntity.notFound().build();
		user.setVerified(true);
		userService.save(user);
		return ResponseEntity.ok().build();
	}
	
	@PostMapping("/changepassword")
	public ResponseEntity<UserDTO> updatePassword(@AuthenticationPrincipal User user, @RequestBody @Valid EditPasswordForm form, Errors errors) throws FormInvalidException {
		if(errors.hasErrors()) {
			throw new FormInvalidException(errors.getAllErrors().get(0).getDefaultMessage());		
		}
		String newPassword = form.getNewPassword();
		boolean oldPasswordValid = BCrypt.checkpw(form.getOldPassword(), user.getPassword());
		if(!oldPasswordValid) {
			throw new FormInvalidException("Old password is incorrect.");
		}
		user.setPassword(encoder.encode(newPassword));
		user = userService.save(user);
		return ResponseEntity.ok(modelMapper.map(user, UserDTO.class));
	}
	
	@PostMapping("/changeemail")
	public ResponseEntity<UserDTO> updateEmail(@AuthenticationPrincipal User user, @RequestBody @Valid EditEmailForm form, Errors errors) throws FormInvalidException, EmailUsedException {
		if(errors.hasErrors()) {
			throw new FormInvalidException(validationErrorMessagesBuilder.getMessagesArray(errors.getAllErrors()));
		}
		String newEmail = form.getEmail();
		User existingUserWithEmail = userService.getByEmail(newEmail);
		if(existingUserWithEmail != null) {
			throw new EmailUsedException("E-mail address is already used.");
		}
		user.setEmail(newEmail);
		user = userService.save(user);
		return ResponseEntity.ok(modelMapper.map(user, UserDTO.class));
	}
	
	@PostMapping("/changeusername")
	public ResponseEntity<UserDTO> updateUsername(@AuthenticationPrincipal User user, @RequestBody @Valid EditUsernameForm form, Errors errors) throws FormInvalidException, UserExistsException {
		if(errors.hasErrors()) {
			throw new FormInvalidException(validationErrorMessagesBuilder.getMessagesArray(errors.getAllErrors()));
		}
		String newUsername = form.getUsername();
		User existingUser = userService.getByUsername(newUsername);
		if(existingUser != null) {
			throw new UserExistsException("User with name " + newUsername + " already exists.");
		}
		user.setUsername(newUsername);
		user = userService.save(user);
		return ResponseEntity.ok(modelMapper.map(user, UserDTO.class));
	}
	
	@PostMapping("/changedata")
	public ResponseEntity<UserDTO> updateData(@AuthenticationPrincipal User user, @RequestBody @Valid EditPersonalDataForm form, Errors errors) throws FormInvalidException {
		if(errors.hasErrors()) {
			throw new FormInvalidException(validationErrorMessagesBuilder.getMessagesArray(errors.getAllErrors()));
		}
		user.setEmail(form.getEmail());
		user = userService.save(user);
		return ResponseEntity.ok(modelMapper.map(user, UserDTO.class));
	}
	
	@PostMapping(path = "/changeprofilepicture")
	public ResponseEntity<UserDTO> updateProfilePicture(HttpServletRequest request, ProfilePictureForm form, @RequestParam("image") MultipartFile image) throws IOException {
		String fileName = StringUtils.cleanPath(image.getOriginalFilename());
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User user = (User) authentication.getPrincipal();
		String imageUrl = PROFILE_PIC_UPLOAD_DIR + "/" + user.getId() + "/" + fileName;
		FileUploadUtil.saveFile(new FileSystemResource("").getFile().getAbsolutePath() + PROFILE_PIC_UPLOAD_DIR + "/" + user.getId(), fileName, image);
		
		String oldPicture = user.getProfilePicture();
		String fullDeletePath = new File(".").getAbsolutePath() + oldPicture;
		if(oldPicture != null && !(new File(fullDeletePath).delete())) { 
			log.error("Error deleting file at path: " + fullDeletePath);
		}
		user.setProfilePicture(imageUrl);
		userService.save(user);
		return ResponseEntity.ok(modelMapper.map(user, UserDTO.class));
	}
	
	@GetMapping("/notifications")
	public ResponseEntity<NotificationsResponse> getNotifications(@AuthenticationPrincipal User user, @RequestParam(name = "onlyCount", defaultValue = "false") boolean onlyCount, @RequestParam(name = "onlyUnread", defaultValue = "false") boolean onlyUnread) {
		NotificationsResponse response = new NotificationsResponse();
		Long count = onlyUnread ? notificationService.countAllUnreadByUser(user) : notificationService.countAllByUser(user);
		response.setCount(count);
		if(onlyCount) {
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
		List<Notification> notifications = onlyUnread ? notificationService.getAllUnreadByUser(user) : notificationService.getAllByUser(user);
		response.setNotifications(convert(notifications));
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@PostMapping("/notifications-mark-single-read")
	public ResponseEntity<HttpStatus> markNotificationAsRead(@AuthenticationPrincipal User user, @RequestParam(name = "id") Long notificationId) {
		Notification notification = notificationService.getById(notificationId);
		if(notification == null) { log.info("X"); return ResponseEntity.notFound().build(); }
		if(!notification.getUser().getId().equals(user.getId())) { log.info("Y"); return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); }
		if(notification.getRead().booleanValue()) { log.info("Z"); return ResponseEntity.ok().build(); }
		notification.setRead(true);
		notificationService.save(notification);
		return ResponseEntity.ok().build();
	}
	
	private List<NotificationDTO> convert(List<Notification> notifications) {
		return notifications.stream().map(this::prepareNotificationDTO).collect(Collectors.toList());
	}
	
	private NotificationDTO prepareNotificationDTO(Notification notification) {
		if(notification == null) throw new IllegalArgumentException("Notification null");
		return modelMapper.map(notification, NotificationDTO.class);
	}
	
	private boolean isExpired(@NonNull VerificationCode verificationCode) {
		Date currentDate = new Date();
		Date codeCreationDate = verificationCode.getCreationDate();
		long diff = currentDate.getTime() - codeCreationDate.getTime();
		return diff > 60000;
	}

}
