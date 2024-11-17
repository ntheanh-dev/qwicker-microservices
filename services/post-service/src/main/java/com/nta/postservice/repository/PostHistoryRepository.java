package com.nta.postservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nta.postservice.entity.PostHistory;

@Repository
public interface PostHistoryRepository extends JpaRepository<PostHistory, String> {}
