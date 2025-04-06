package com.nta.notification.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "notification")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
    @Id
    private String id;
    private String recipient;
    private String message;
    private String notificationType; // e.g., "OTP_REGISTER"
    private String status; // e.g., "SENT", "FAILED"
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
