package com.nta.postservice.entity;

import java.time.LocalDateTime;
import java.util.Set;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import com.nta.postservice.enums.DeliveryTimeType;
import com.nta.postservice.enums.PostStatus;

import lombok.*;

@Entity
@Setter
@Getter
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "post_id", nullable = false)
    String id;

    String description;
    LocalDateTime postTime;
    DeliveryTimeType deliveryTimeType;

    @Enumerated(EnumType.STRING)
    PostStatus status;

    @OneToOne
    @JoinColumn(name = "product_id")
    Product product;

    @NotNull
    String pickupLocationId;

    LocalDateTime pickupDatetime;

    @NotNull
    String dropLocationId;

    LocalDateTime dropDateTime;

    String userId;

    @ManyToOne
    @JoinColumn(name = "vehicle_id")
    Vehicle vehicleType;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "post")
    Set<PostHistory> history;
}
