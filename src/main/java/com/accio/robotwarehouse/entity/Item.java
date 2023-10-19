package com.accio.robotwarehouse.entity;

import javax.persistence.*;
import java.time.LocalTime; // Import LocalTime instead of LocalDateTime

@Entity
@Table(name = "items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "item_id")
    private long id;

    @Column(name = "completion_time")
    private LocalTime completionTime;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
    @ManyToOne
    @JoinColumn(name = "robot_id")
    private Robot robot;

    public Item() {
    }

    public Item(long id, LocalTime completionTime, Order order, Robot robot) {
        this.id = id;
        this.completionTime = completionTime;
        this.order = order;
        this.robot = robot;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalTime getCompletionTime() {
        return completionTime;
    }

    public void setCompletionTime(LocalTime completionTime) {
        this.completionTime = completionTime;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Robot getRobot() {
        return robot;
    }

    public void setRobot(Robot robot) {
        this.robot = robot;
    }
}
