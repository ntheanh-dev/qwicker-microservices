package com.nta.profileservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nta.profileservice.entity.Vehicle;

public interface VehicleRepository extends JpaRepository<Vehicle, String> {
    Optional<Vehicle> findById(String id);
}
