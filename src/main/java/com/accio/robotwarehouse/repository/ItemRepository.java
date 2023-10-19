package com.accio.robotwarehouse.repository;

import com.accio.robotwarehouse.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
}
