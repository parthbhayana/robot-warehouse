package com.accio.robotwarehouse.repository;

import com.accio.robotwarehouse.entity.PackingStation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PackingStationRepository extends JpaRepository<PackingStation, Long> {
}
