package com.nta.identity.entity;

import jakarta.persistence.*;
import java.util.Set;
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

    @Column(length = 15, columnDefinition = "varchar(25) default 'OFFLINE'")
    @Enumerated(EnumType.STRING)
    AccountStatus status;
}
