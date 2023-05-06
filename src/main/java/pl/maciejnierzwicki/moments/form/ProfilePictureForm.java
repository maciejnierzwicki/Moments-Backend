package pl.maciejnierzwicki.moments.form;


import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class ProfilePictureForm {
	
	private MultipartFile image;

}
