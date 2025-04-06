package com.nta.notification.service;

import com.nta.event.dto.NotificationEvent;
import com.nta.notification.dto.request.EmailRequest;
import com.nta.notification.dto.request.Recipient;
import com.nta.notification.dto.request.SendEmailRequest;
import com.nta.notification.dto.request.Sender;
import com.nta.notification.dto.response.EmailResponse;
import com.nta.notification.entity.Notification;
import com.nta.notification.repository.NotificationRepository;
import com.nta.notification.repository.httpclient.EmailClient;
import feign.FeignException;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class EmailService {
    EmailClient emailClient;
    Configuration freeMakerConfig;
    NotificationRepository notificationRepository;
    @Value("${notification.email.brevo-apikey}")
    @NonFinal
    String APIKEY;

    public EmailResponse sendEmail(final SendEmailRequest sendEmailRequest) {
        final EmailRequest emailRequest = EmailRequest.builder()
                .sender(Sender.builder()
                        .name("Qwicker")
                        .email("theanhmgt66@gmail.com")
                        .build())
                .to(List.of(sendEmailRequest.getTo()))
                .subject(sendEmailRequest.getSubject())
                .htmlContent(sendEmailRequest.getHtmlContent())
                .build();
        try {
            return emailClient.sendEmail(APIKEY, emailRequest);
        } catch (final FeignException exception) {
            log.error("Cannot send email:", exception);
            return null;
        }
    }

    public void sendOTP(final NotificationEvent message) {
        try {
            final Recipient recipient =
                    Recipient.builder().email(message.getRecipient()).build();
            final Template template = freeMakerConfig.getTemplate(message.getTemplateCode());
            final String htmlContent = FreeMarkerTemplateUtils.processTemplateIntoString(template, message.getParam());
            final String OTP_EMAIL_SUBJECT = "Mã OTP Xác Thực Cho Tài Khoản Của Bạn";
            final SendEmailRequest sendEmailRequest = SendEmailRequest.builder()
                    .to(recipient)
                    .subject(OTP_EMAIL_SUBJECT)
                    .htmlContent(htmlContent)
                    .build();
            final EmailResponse response = this.sendEmail(sendEmailRequest);
            // TODO: Maybe store sent mailId to database

            // Save to MongoDB
            final Notification notification = Notification.builder()
                    .recipient(message.getRecipient())
                    .message("Your OTP code is: " + message.getParam().get("otp"))
                    .notificationType("OTP_REGISTER")
                    .status("SENT")
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            notificationRepository.save(notification);

        } catch (IOException | TemplateException e) {
            log.error("Cannot get template email with exception: ", e);
        }
    }
}
