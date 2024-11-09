package com.nta.notification.controller;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Controller;

import com.nta.event.dto.NotificationEvent;
import com.nta.notification.dto.request.Recipient;
import com.nta.notification.dto.request.SendEmailRequest;
import com.nta.notification.service.EmailService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Controller
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class NotificationController {
    EmailService emailService;

    @KafkaListener(topics = "notification-delivery")
    public void listenNotificationDelivery(final NotificationEvent message) {
        final Recipient recipient =
                Recipient.builder().email(message.getRecipient()).build();
        final SendEmailRequest sendEmailRequest = SendEmailRequest.builder()
                .to(recipient)
                .subject("OTP TEST")
                .htmlContent("Opt cua ban la: " + message.getParam().get("otp"))
                .build();
        emailService.sendEmail(sendEmailRequest);
    }
}
