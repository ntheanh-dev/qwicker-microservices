package com.nta.postservice.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.nta.postservice.enums.PostStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class PostHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "post_history_id",nullable = false)
    String id;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    Post post;
    @CreatedDate
    @Column(updatable = false, nullable = false)
    LocalDateTime statusChangeDate;
    String description;
    String photo;

    @Enumerated(EnumType.STRING)
    PostStatus status;
}
