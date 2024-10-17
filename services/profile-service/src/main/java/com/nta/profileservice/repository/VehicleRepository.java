package com.nta.profileservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nta.profileservice.entity.Vehicle;

import java.util.Optional;

public interface VehicleRepository extends JpaRepository<Vehicle, String> {
    Optional<Vehicle> findById(String id);
}
