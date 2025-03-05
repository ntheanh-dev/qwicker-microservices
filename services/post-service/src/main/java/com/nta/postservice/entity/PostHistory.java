package com.nta.postservice.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.nta.postservice.enums.PostHistoryStatus;

import lombok.*;

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
    @Column(name = "post_history_id", nullable = false)
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
    PostHistoryStatus status;
}
