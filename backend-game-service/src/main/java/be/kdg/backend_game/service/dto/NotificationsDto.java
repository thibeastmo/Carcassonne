package be.kdg.backend_game.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
@Getter
@AllArgsConstructor
public class NotificationsDto {
    private int amountOfNotifications;
    private List<NotificationDto> notificationDtos;
}
