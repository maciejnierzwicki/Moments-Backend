package pl.maciejnierzwicki.moments.mail;

import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import pl.maciejnierzwicki.moments.model.passwordresetcode.PasswordResetCode;
import pl.maciejnierzwicki.moments.model.verificationcode.VerificationCode;


@Service
@Slf4j
public class MailService {
	
	@Autowired
	private JavaMailSender mailSender;
	
	@Value("${moments.mail.accountActivationFromID}")
	private String accountActivationFrom;
	@Value("${moments.mail.passwordResetFromID}")
	private String passwordResetFrom;
    
    public void sendActivationMail(VerificationCode verificationCode) {
 	    //create a MimeMessage object
 	    MimeMessage message = mailSender.createMimeMessage();
 	    try {
	 	    //set From email field
	 	    message.setFrom(new InternetAddress(accountActivationFrom));
	 	    //set To email field
	 	    message.setRecipients(Message.RecipientType.TO,
	 	               InternetAddress.parse(verificationCode.getUser().getEmail()));
	 	    //set email subject field
	 	    message.setSubject("Moments - Account verification");
	 	    //set the content of the email message
	 	    message.setContent("<html lang=\"pl\"><head><meta charset=\"utf-8\"/></head><body style=\"font-family:Helvetica; font-size: 16px\">Welcome to Moments! <br> Please use code <b>" + verificationCode.getCode() + "</b> in order to activate account.<br>Your username: <b>" + verificationCode.getUser().getUsername() + "</b></body></html>", "text/html; charset=utf-8");
	 	    //send the email message
	 	    mailSender.send(message);   
 	    }
 	    catch(MailException | MessagingException ex) {
 	    	log.error(ex.getMessage());
 	    }
 	}
    
    public void sendPasswordResetMail(PasswordResetCode passwordResetCode) {
    	MimeMessage message = mailSender.createMimeMessage();
    	try {
	 	    //create a MimeMessage object
	 	    
	 	    //set From email field
	 	    message.setFrom(new InternetAddress(passwordResetFrom));
	 	    //set To email field
	 	    message.setRecipients(Message.RecipientType.TO,
	 	               InternetAddress.parse(passwordResetCode.getUser().getEmail()));
	 	    //set email subject field
	 	    message.setSubject("Moments - Password reset");
	 	    //set the content of the email message
	 	    message.setContent("<html lang=\"pl\"><head><meta charset=\"utf-8\"/></head><body style=\"font-family:Helvetica; font-size: 16px\">A password request has been sent for account with email address <b>" + passwordResetCode.getUser().getEmail() + "</b>. Password reset code is <b>" + passwordResetCode.getCode() + "</b> and expires in one minute. <br>Enter this code in password reset form to reset your password.<br>If you didn't requested password reset please ignore this message.</body></html>", "text/html; charset=utf-8");
	 	    //send the email message
	 	    mailSender.send(message);
	 	    log.debug("Email Message Sent Successfully");
	 	      } catch (MessagingException | MailException e) {
	 	         log.error(e.getMessage());
	 	      }
  }
	
}
