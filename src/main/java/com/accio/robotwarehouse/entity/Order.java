package com.accio.robotwarehouse.entity;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "order_id")
    private long id;

    @Column(name = "generated_time")
    public LocalTime generatedTime;

    @Column(name = "completion_time_mode_1")
    public LocalTime completionTimeMode1;

    @Column(name = "completion_time_mode_2")
    public LocalTime completionTimeMode2;

    @Column(name = "pickup_node")
    private String pickupNode;

    @OneToMany(mappedBy = "order")
    public List<Item> orderItems = new ArrayList<>();

    @OneToOne(mappedBy = "order")
    private PackingStation packingStation;

    public Order() {
    }

    public Order(long id, LocalTime generatedTime, LocalTime completionTimeMode1, LocalTime completionTimeMode2, String pickupNode, List<Item> orderItems, PackingStation packingStation) {
        this.id = id;
        this.generatedTime = generatedTime;
        this.completionTimeMode1 = completionTimeMode1;
        this.completionTimeMode2 = completionTimeMode2;
        this.pickupNode = pickupNode;
        this.orderItems = orderItems;
        this.packingStation = packingStation;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalTime getGeneratedTime() {
        return generatedTime;
    }

    public void setGeneratedTime(LocalTime generatedTime) {
        this.generatedTime = generatedTime;
    }

    public LocalTime getCompletionTimeMode1() {
        return completionTimeMode1;
    }

    public void setCompletionTimeMode1(LocalTime completionTimeMode1) {
        this.completionTimeMode1 = completionTimeMode1;
    }

    public LocalTime getCompletionTimeMode2() {
        return completionTimeMode2;
    }

    public void setCompletionTimeMode2(LocalTime completionTimeMode2) {
        this.completionTimeMode2 = completionTimeMode2;
    }

    public List<Item> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<Item> orderItems) {
        this.orderItems = orderItems;
    }

    public PackingStation getPackingStation() {
        return packingStation;
    }

    public void setPackingStation(PackingStation packingStation) {
        this.packingStation = packingStation;
    }

    public String getPickupNode() {
        return pickupNode;
    }

    public void setPickupNode(String pickupNode) {
        this.pickupNode = pickupNode;
    }
}
