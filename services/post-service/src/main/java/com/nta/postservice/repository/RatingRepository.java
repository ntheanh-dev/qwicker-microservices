package com.nta.postservice.repository;

import com.nta.postservice.entity.Rating;
import com.nta.postservice.entity.RatingId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingRepository extends JpaRepository<Rating, RatingId> {
    List<Rating> findByShipperId(String shipperId);
}
