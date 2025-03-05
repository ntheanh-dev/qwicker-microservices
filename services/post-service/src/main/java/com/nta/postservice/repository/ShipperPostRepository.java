package com.nta.postservice.repository;

import com.nta.postservice.entity.Post;
import com.nta.postservice.entity.ShipperPost;
import com.nta.postservice.entity.ShipperPostId;
import com.nta.postservice.enums.PostStatus;
import com.nta.postservice.enums.ShipperPostStatus;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ShipperPostRepository extends JpaRepository<ShipperPost, ShipperPostId> {
    @Query("SELECT sp FROM ShipperPost sp WHERE sp.post.id = :postId ORDER BY sp.invitedAt DESC")
    Optional<ShipperPost> getLastShipperPostByPostId(@Param("postId") String postId);

    Optional<ShipperPost> findByPostAndShipper(Post post, String shipper);

    boolean existsByPost_IdAndShipper(final String post_id, final String shipper);

    List<ShipperPost> findByPostIdAndStatus(final String postId, final ShipperPostStatus status);

    @Query(
            "SELECT sp.post.id FROM ShipperPost sp WHERE sp.status = :status AND sp.shipper = :shipper "
                    + "AND DATE_FORMAT(sp.joinedAt, '%Y-%m-%d %H:00:00') "
                    + "BETWEEN DATE_FORMAT(:startDate, '%Y-%m-%d %H:00:00') "
                    + "AND DATE_FORMAT(:endDate, '%Y-%m-%d %H:00:00')")
    List<String> findPostIdsByShipperAndStatusAndDateRangeHourly(
            @Param("shipper") String shipper,
            @Param("status") ShipperPostStatus status,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    @Query(
            "SELECT sp.post.id FROM ShipperPost sp WHERE sp.status = :status AND sp.shipper = :shipper "
                    + "AND DATE(sp.joinedAt) "
                    + "BETWEEN DATE(:startDate) "
                    + "AND DATE(:endDate)")
    List<String> findPostIdsByShipperAndDateRangeAndAcceptedDaily(
            @Param("shipper") String shipper,
            @Param("status") ShipperPostStatus status,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    @Query(
            "SELECT sp.post.id FROM ShipperPost sp WHERE sp.status = :status AND sp.shipper = :shipper "
                    + "AND DATE_FORMAT(sp.joinedAt, '%Y-%m') "
                    + "BETWEEN DATE_FORMAT(:startDate, '%Y-%m') "
                    + "AND DATE_FORMAT(:endDate, '%Y-%m')")
    List<String> findPostIdsByShipperAndStatusAndDateRangeMonthly(
            @Param("shipper") String shipper,
            @Param("status") ShipperPostStatus status,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    @Query(
            "SELECT sp.post FROM ShipperPost sp WHERE sp.shipper = :shipperId AND sp.post.status IN :statusList")
    List<Post> findPostsByShipperIdAndStatus(
            @Param("shipperId") String shipperId, @Param("statusList") List<PostStatus> statusList);
}
