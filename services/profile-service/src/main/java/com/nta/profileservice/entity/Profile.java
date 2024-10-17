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
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "profile_id")
    String id;
    @Column(nullable = false, length = 150) // one account may have multiple profiles like fb,google ...
    String accountId;
    @Column(length = 50)
    String firstName;
    @Column(length = 50)
    String lastName;
    @Column(length = 150)
    String avatar;
}
