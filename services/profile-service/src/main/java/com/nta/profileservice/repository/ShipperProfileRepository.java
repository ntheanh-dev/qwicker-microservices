package com.nta.profileservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nta.profileservice.entity.ShipperProfile;

public interface ShipperProfileRepository extends JpaRepository<ShipperProfile, String> {}
