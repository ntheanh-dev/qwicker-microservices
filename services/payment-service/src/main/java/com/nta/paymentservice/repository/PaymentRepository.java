package com.nta.paymentservice.repository;

import com.nta.paymentservice.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, String> {

    Optional<Payment> findByPostId(String postId);

    @Query("SELECT p FROM Payment p WHERE p.postId IN :postIds")
    List<Payment> findByPostIds(@Param("postIds") List<String> postIds);

    @Query(
            "SELECT "
                    + "DATE_FORMAT(pm.paidAt, '%Y-%m-%d %H:00:00') AS dateTime, "
                    + "COUNT(pm.id) AS totalPayments, "
                    + "SUM(pm.price) AS totalRevenue, "
                    + "SUM(CASE WHEN pm.paymentMethod = 'CASH' THEN pm.price ELSE 0 END) AS cashRevenue, "
                    + "SUM(CASE WHEN pm.paymentMethod = 'VNPAY' THEN pm.price ELSE 0 END) AS vnPayRevenue "
                    + "FROM Payment pm WHERE pm.postId IN :postIds AND "
                    + "pm.paidAt BETWEEN :startDate AND :endDate "
                    + "GROUP BY DATE_FORMAT(pm.paidAt, '%Y-%m-%d %H:00:00') "
                    + "ORDER BY DATE_FORMAT(pm.paidAt, '%Y-%m-%d %H:00:00')")
    List<Object[]> findHourlyIncome(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("postIds") List<String> postIds);

    @Query(
            "SELECT "
                    + "DATE(pm.paidAt) AS dateTime, "
                    + "COUNT(pm.id) AS totalPayments, "
                    + "SUM(pm.price) AS totalRevenue, "
                    + "SUM(CASE WHEN pm.paymentMethod = 'CASH' THEN pm.price ELSE 0 END) AS cashRevenue, "
                    + "SUM(CASE WHEN pm.paymentMethod = 'VNPAY' THEN pm.price ELSE 0 END) AS vnPayRevenue "
                    + "FROM Payment pm WHERE pm.postId IN :postIds AND "
                    + "pm.paidAt BETWEEN :startDate AND :endDate "
                    + "GROUP BY DATE(pm.paidAt) "
                    + "ORDER BY DATE(pm.paidAt)")
    List<Object[]> findDailyIncome(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("postIds") List<String> postIds);

    @Query(
            "SELECT "
                    + "DATE_FORMAT(pm.paidAt, '%Y-%m') AS dateTime, "
                    + "COUNT(pm.id) AS totalPayments, "
                    + "SUM(pm.price) AS totalRevenue, "
                    + "SUM(CASE WHEN pm.paymentMethod = 'CASH' THEN pm.price ELSE 0 END) AS cashRevenue, "
                    + "SUM(CASE WHEN pm.paymentMethod = 'VNPAY' THEN pm.price ELSE 0 END) AS vnPayRevenue "
                    + "FROM Payment pm WHERE pm.postId IN :postIds AND "
                    + "pm.paidAt BETWEEN :startDate AND :endDate "
                    + "GROUP BY DATE_FORMAT(pm.paidAt, '%Y-%m') "
                    + "ORDER BY DATE_FORMAT(pm.paidAt, '%Y-%m')")
    List<Object[]> findMonthlyIncome(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("postIds") List<String> postIds);

    @Query(
            "SELECT  DATE_FORMAT(pm.paidAt, '%m-%Y') AS month, " +
                    "pm.paymentMethod AS paymentMethod, " +
                    "SUM(pm.price) AS totalAmount " +
                    "FROM Payment pm GROUP BY month, pm.paymentMethod " +
                    "ORDER BY DATE_FORMAT(pm.paidAt, '%m-%Y')  DESC")
    List<Object[]> getMonthlyPaymentStatistics();
}
