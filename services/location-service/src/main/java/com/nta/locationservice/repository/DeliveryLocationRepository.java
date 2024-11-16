package com.nta.locationservice.repository;

import com.nta.locationservice.entity.DeliveryLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryLocationRepository extends JpaRepository<DeliveryLocation, String> {
}
