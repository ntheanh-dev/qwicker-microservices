package com.nta.profileservice.entity;

import jakarta.persistence.*;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Vehicle {
    @Id
    @Column(name = "vehicle_id", nullable = false)
    String id;

    String capacity;
    String description;
    String icon;

    @Column(length = 50)
    String name;
}
