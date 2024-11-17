package com.nta.paymentservice.entity;
import com.nta.paymentservice.enums.PaymentMethod;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "payment_id",nullable = false)
    String id;
    BigDecimal price;
    LocalDateTime paidAt;
    boolean isPosterPay;
    @Column(nullable = false, updatable = false, unique = true, length = 100)
    String postId;
    @Enumerated(EnumType.STRING)
    PaymentMethod paymentMethod;

    @CreatedDate
    @Column(updatable = false, nullable = false)
    LocalDateTime createdDate;
}