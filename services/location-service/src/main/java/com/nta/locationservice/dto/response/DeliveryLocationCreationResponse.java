package com.nta.locationservice.dto.response;


import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DeliveryLocationCreationResponse {
    String id;
    String contact;
    String phoneNumber;
    String addressLine;
    String formattedAddress;
    String postalCode;
    double latitude;
    double longitude;
}
