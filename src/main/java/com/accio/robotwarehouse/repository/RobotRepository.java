package com.accio.robotwarehouse.repository;

import com.accio.robotwarehouse.entity.Robot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RobotRepository extends JpaRepository<Robot, Long> {
}
