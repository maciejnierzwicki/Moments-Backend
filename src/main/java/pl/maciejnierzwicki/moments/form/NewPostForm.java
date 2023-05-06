package pl.maciejnierzwicki.moments.form;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class NewPostForm {
	
	private MultipartFile image;
	private String description;

}
