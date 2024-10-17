package com.nta.identity.entity;

import java.time.LocalDate;
import java.util.Set;

import jakarta.persistence.*;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(nullable = false, length = 30, unique = true)
    String username;
    @Column(nullable = false)
    String password;
    @Column(length = 30, unique = true)
    String email;
    @ManyToMany
    Set<Role> roles;
}
