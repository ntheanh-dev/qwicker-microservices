package com.nta.postservice.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.*;

@Entity
@Setter
@Getter
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@IdClass(RatingId.class)
public class Rating {
    @Id
    @Column(name = "shipper", nullable = false)
    String shipperId;

    @Id
    @Column(name = "rater", nullable = false)
    String raterId;

    @Id
    @Column(name = "post", nullable = false)
    String postId;

    @NotNull
    double rating;

    @NotNull
    String feedback;

    LocalDateTime createdAt;

    @JsonBackReference
    @JoinColumn(name = "post_id", nullable = false)
    @OneToOne(fetch = FetchType.EAGER)
    Post post;
}
