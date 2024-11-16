package com.nta.postservice.entity;
import com.nta.postservice.enums.ShipperPostStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "shipper_post")
public class ShipperPost {
    @Id
    @Column(name = "shipper", nullable = false)
    String shipper;
    @Id
    @Column(name = "post", nullable = false)
    String post;
    @Enumerated(EnumType.STRING)
    ShipperPostStatus status;
    LocalDateTime joinedAt;
}
