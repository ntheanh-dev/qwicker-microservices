package com.nta.locationservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class DeliveryLocation {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id",nullable = false)
    String id;
    String contact;
    @NotNull
    String phoneNumber;
    String addressLine;
    String formattedAddress;
    String postalCode;
    double latitude;
    double longitude;
}
