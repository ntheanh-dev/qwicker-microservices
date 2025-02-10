package com.nta.postservice.repository;

import com.nta.postservice.entity.Rating;
import com.nta.postservice.entity.RatingId;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RatingRepository extends JpaRepository<Rating, RatingId> {
  List<Rating> findByShipperId(String shipperId);

  @Query("SELECT COALESCE(AVG(r.rating), 0) FROM Rating r WHERE r.shipperId = :shipperId")
  Double findAverageRatingByShipperId(@Param("shipperId") String shipperId);
}
