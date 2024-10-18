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
public class ShipperProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "shipper_profile_id")
    String id;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    Profile profile;

    @Column(length = 15)
    String vehicleNumber;

    @ManyToOne
    @JoinColumn(name = "vehicle_id")
    Vehicle vehicle;

    @Column(name = "identity_f", length = 120)
    String identityF;

    @Column(name = "identity_b", length = 120)
    String identityB;
}
