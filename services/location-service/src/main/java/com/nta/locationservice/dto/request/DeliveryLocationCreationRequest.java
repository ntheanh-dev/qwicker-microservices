package com.nta.locationservice.dto.request;


import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DeliveryLocationCreationRequest {
    String contact;
    String phoneNumber;
    String addressLine;
    String formattedAddress;
    String postalCode;
    double latitude;
    double longitude;
}
