package com.nta.postservice.repository;

import com.nta.postservice.entity.Post;
import com.nta.postservice.entity.ShipperPost;
import com.nta.postservice.entity.ShipperPostId;
import com.nta.postservice.enums.ShipperPostStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShipperPostRepository extends JpaRepository<ShipperPost, ShipperPostId> {
    @Query("SELECT sp FROM ShipperPost sp WHERE sp.post.id = :postId ORDER BY sp.invitedAt DESC")
    Optional<ShipperPost> getLastShipperPostByPostId(@Param("postId") String postId);

    Optional<ShipperPost> findByPostAndShipper(Post post, String shipper);

    boolean existsByPostIdAndShipper(final String post_id, final String shipper);

    List<ShipperPost> findByPostIdAndStatus(final String postId, final ShipperPostStatus status);
}
