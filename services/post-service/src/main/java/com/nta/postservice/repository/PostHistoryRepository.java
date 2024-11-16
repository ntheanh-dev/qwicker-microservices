package com.nta.postservice.repository;

import com.nta.postservice.entity.PostHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostHistoryRepository extends JpaRepository<PostHistory, String> {
}
