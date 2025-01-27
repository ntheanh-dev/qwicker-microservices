package com.nta.postservice.repository;

import com.nta.postservice.entity.ShipperPost;
import com.nta.postservice.entity.ShipperPostId;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ShipperPostRepository extends JpaRepository<ShipperPost, ShipperPostId> {
  boolean existsByShipperAndPostId(String shipperId, String postId);

  @Query("SELECT COUNT(sP) FROM ShipperPost sP WHERE sP.post.id = :postId")
  Integer countShipperJoinedByPostId(@Param("postId") String postId);

  @Query("SELECT sp FROM ShipperPost sp WHERE sp.post.id = :postId ORDER BY sp.invitedAt DESC")
  Optional<ShipperPost> getLastShipperPostByPostId(@Param("postId") String postId);
}
