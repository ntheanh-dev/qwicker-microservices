package com.nta.paymentservice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.nta.paymentservice.entity.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, String> {
    Optional<Payment> findByPostId(String postId);

    @Query("SELECT p FROM Payment p WHERE p.postId IN :postIds")
    List<Payment> findByPostIds(@Param("postIds") List<String> postIds);
}
