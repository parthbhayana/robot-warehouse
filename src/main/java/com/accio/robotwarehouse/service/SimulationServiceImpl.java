package com.accio.robotwarehouse.service;

import com.accio.robotwarehouse.entity.Item;
import com.accio.robotwarehouse.entity.Order;
import com.accio.robotwarehouse.entity.PackingStation;
import com.accio.robotwarehouse.repository.OrderRepository;
import com.accio.robotwarehouse.repository.RobotRepository;
import com.accio.robotwarehouse.repository.PackingStationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import java.io.Writer;
import java.io.FileWriter;
import java.util.concurrent.ThreadLocalRandom;

import com.opencsv.CSVWriter;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SimulationServiceImpl implements SimulationService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private RobotRepository robotRepository;

    @Autowired
    private PackingStationRepository packingStationRepository;

    @Override
    @Transactional
    public void startSimulation() {
        List<Order> orders = generateOrders();
        for (Order order : orders) {
            simulateWholeOrderPicking(order);
            simulateItemPicking(order);
        }
        generatePerformanceReport();
    }

    @Override
    @Transactional
    public List<Order> generateOrders() {
        List<Order> orders = new ArrayList<>();
        ThreadLocalRandom random = ThreadLocalRandom.current();
        int meanIntervalSeconds = 30;
        int variance = 25;
        int packingStations = 3;
        LocalTime currentTime = LocalTime.now();
        int packingStationIndex = 0;
        LocalTime endTime = currentTime.plus(1, ChronoUnit.HOURS);

        while (currentTime.isBefore(endTime)) {
            Order order = new Order();
            order.setGeneratedTime(currentTime);
            PackingStation packingStation = packingStationRepository.findById((long) (packingStationIndex + 1)).orElse(null);
            if (packingStation == null) {
            }
            order.setPackingStation(packingStation);
            packingStationIndex = (packingStationIndex + 1) % packingStations;
            int meanItems = 6;
            int varianceItems = 9;
            int items = meanItems + random.nextInt(2 * varianceItems + 1) - varianceItems;
            List<Item> orderItems = new ArrayList<>();
            for (int i = 1; i <= items; i++) {
                Item item = new Item();
                orderItems.add(item);
            }
            order.setOrderItems(orderItems);
            String pickupNode = determinePickupNodeFromStorageNodes();
            order.setPickupNode(pickupNode);
            orders.add(order);
            int strictVariance = Math.max(1, variance - meanIntervalSeconds);
            int nextOrderInterval = meanIntervalSeconds + random.nextInt(-strictVariance, strictVariance);
            currentTime = currentTime.plusSeconds(nextOrderInterval);
        }

        return orders;
    }

    private int currentStorageNodeIndex = 0;

    private String determinePickupNodeFromStorageNodes() {
        String storageNodesLayout = "6 12 18 24 30 36 42 48 54\n" + "5 11 17 23 29 35 41 47 53\n" + "4 10 16 22 28 34 40 46 52\n" + "3 9 15 21 27 33 39 45 51\n" + "2 8 14 20 26 32 38 44 50\n";
        String[] lines = storageNodesLayout.split("\n");
        int randomLineIndex = (int) (Math.random() * lines.length);
        String randomLine = lines[randomLineIndex];
        String[] nodeValues = randomLine.split(" ");
        int randomNodeIndex = (int) (Math.random() * nodeValues.length);
        String pickupNode = nodeValues[randomNodeIndex];
        return pickupNode;
    }

    private String rotateStorageNodesLayout(String layout) {
        String[] lines = layout.split("\n");
        String[] rotatedLines = new String[lines.length];
        for (int i = 0; i < lines.length; i++) {
            String[] nodeValues = lines[i].split(" ");
            String[] rotatedNodeValues = new String[nodeValues.length];
            for (int j = 0; j < nodeValues.length; j++) {
                rotatedNodeValues[(j + 1) % nodeValues.length] = nodeValues[j];
            }
            rotatedLines[i] = String.join(" ", rotatedNodeValues);
        }
        return String.join("\n", rotatedLines);
    }

    private String determinePickupNode(PackingStation packingStation) {
        if (packingStation != null) {
            long packingStationId = packingStation.getId();
            String pickupNode = "PickupNode_" + packingStationId;
            return pickupNode;
        } else {
            return "Unknown";
        }
    }

    @Override
    @Transactional
    public void simulateWholeOrderPicking(Order order) {
        int itemsCount = order.getOrderItems().size();
        int pickingTime = itemsCount * 5;

        if (isWholeOrderPickingMode(order)) {
            int K = itemsCount;
            int packingTime = K * 5;
            int totalOrderTime = pickingTime + packingTime;
            LocalTime completionTime = order.getGeneratedTime().plusSeconds(totalOrderTime);
            order.setCompletionTimeMode1(completionTime);
        } else {
            order.setCompletionTimeMode1(order.getGeneratedTime());
        }
        orderRepository.save(order);
    }

    @Override
    @Transactional
    public void simulateItemPicking(Order order) {
        List<Item> items = order.getOrderItems();
        int totalPickingTime = 0;
        int totalPackingTime = 0;

        for (Item item : items) {
            int pickingTime = 5;
            totalPickingTime += pickingTime;
            int packingTime = 5;
            LocalTime itemCompletionTime = order.getGeneratedTime().plusSeconds(totalPickingTime);
            item.setCompletionTime(itemCompletionTime);
            totalPackingTime += packingTime;
        }

        int totalOrderTime = totalPickingTime + totalPackingTime;
        LocalTime orderCompletionTime = order.getGeneratedTime().plusSeconds(totalOrderTime);
        order.setCompletionTimeMode2(orderCompletionTime);
        orderRepository.save(order);
    }

    @Override
    @Transactional
    public boolean isWholeOrderPickingMode(Order order) {
        return order.getOrderItems().size() == 1;
    }

    @Override
    @Transactional
    public void generatePerformanceReport() {
        List<Order> orders = orderRepository.findAll();
        String csvFilePath = "performance_report.csv";

        try (Writer writer = new FileWriter(csvFilePath)) {
            CSVWriter csvWriter = new CSVWriter(writer);
            String[] header = {"Order Num", "Pickup Node", "Completed Time Mode 1", "Completed Time Mode 2"};
            csvWriter.writeNext(header);

            for (Order order : orders) {
                if (order != null && order.getOrderItems() != null && !order.getOrderItems().isEmpty()) {
                    int itemsCount = order.getOrderItems().size();
                    int pickingTime = itemsCount * 5;
                    int packingTime = itemsCount * 5;
                    String completionTimeMode1 = order.getCompletionTimeMode1().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
                    String completionTimeMode2 = order.getCompletionTimeMode2().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
                    String[] data = {String.valueOf(order.getId()), order.getPickupNode(), completionTimeMode1, completionTimeMode2};
                    csvWriter.writeNext(data);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
