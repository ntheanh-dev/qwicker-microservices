package com.nta.postservice.entity;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RatingId implements Serializable {
    @Column(name = "shipper", nullable = false)
    private String shipperId;

    @Column(name = "rater", nullable = false)
    private String raterId;

    @Column(name = "post", nullable = false)
    private String postId;
}
