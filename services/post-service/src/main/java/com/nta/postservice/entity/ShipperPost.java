package com.nta.postservice.entity;

import com.nta.postservice.enums.ShipperPostStatus;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@IdClass(ShipperPostId.class)
@Table(name = "shipper_post")
public class ShipperPost {
  @Id
  @Column(name = "shipper", nullable = false)
  String shipper;

  @Id
  @ManyToOne
  @JoinColumn(name = "post_id", nullable = false)
  Post post;

  @Enumerated(EnumType.STRING)
  ShipperPostStatus status;

  @CreatedDate
  @Column(updatable = false)
  LocalDateTime invitedAt;

  LocalDateTime joinedAt;
}
