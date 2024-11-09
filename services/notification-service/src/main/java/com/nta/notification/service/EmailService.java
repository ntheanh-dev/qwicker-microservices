package com.nta.notification.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.nta.notification.dto.request.EmailRequest;
import com.nta.notification.dto.request.SendEmailRequest;
import com.nta.notification.dto.request.Sender;
import com.nta.notification.dto.response.EmailResponse;
import com.nta.notification.repository.httpclient.EmailClient;

import feign.FeignException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class EmailService {
    EmailClient emailClient;

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
            return emailClient.sentEmail(APIKEY, emailRequest);
        } catch (final FeignException exception) {
            log.error("Cannot send email:", exception);
            return null;
        }
    }
}