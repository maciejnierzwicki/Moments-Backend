package pl.maciejnierzwicki.moments.model.notification;

import java.util.List;

import lombok.Data;

@Data
public class NotificationsResponse {
	
	private Long count;
	private List<NotificationDTO> notifications;

}
