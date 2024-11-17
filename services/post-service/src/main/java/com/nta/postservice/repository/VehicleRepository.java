package com.nta.postservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nta.postservice.entity.Vehicle;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, String> {}
