package com.accio.robotwarehouse.service;

import com.accio.robotwarehouse.entity.Order;

import java.util.List;

public interface SimulationService {

    void startSimulation();

    List<Order> generateOrders();

    boolean isWholeOrderPickingMode(Order order);

    void simulateWholeOrderPicking(Order order);

    void simulateItemPicking(Order order);

    void generatePerformanceReport();
}