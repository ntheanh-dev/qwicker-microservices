package com.nta.locationservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nta.locationservice.entity.DeliveryLocation;

@Repository
public interface DeliveryLocationRepository extends JpaRepository<DeliveryLocation, String> {}
