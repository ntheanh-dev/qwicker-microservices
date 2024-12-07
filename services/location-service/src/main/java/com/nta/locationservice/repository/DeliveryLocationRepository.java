package com.nta.locationservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.nta.locationservice.entity.DeliveryLocation;

@Repository
public interface DeliveryLocationRepository extends JpaRepository<DeliveryLocation, String> {
    @Query("SELECT d FROM DeliveryLocation  d WHERE d.id IN :ids")
    List<DeliveryLocation> findAllByIdList(@Param("ids") List<String> ids);
}
