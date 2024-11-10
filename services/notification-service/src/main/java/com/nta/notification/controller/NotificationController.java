package com.nta.notification.controller;

import java.io.IOException;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Controller;

import com.nta.event.dto.NotificationEvent;
import com.nta.notification.service.EmailService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Controller
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class NotificationController {
    EmailService emailService;

    @KafkaListener(topics = "notification-sent-otp")
    public void listenNotificationSentOTP(final NotificationEvent message) throws IOException {
        emailService.sendOTP(message);
    }
}
