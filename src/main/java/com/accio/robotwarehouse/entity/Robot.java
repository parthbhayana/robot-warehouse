package com.accio.robotwarehouse.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Robot {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id; // Unique robot identifier (using long)

    private boolean available;

    @OneToMany(mappedBy = "robot")
    private List<Item> assignedItems = new ArrayList<>();; // List of items assigned to the robot

    public Robot() {
    }

    public Robot(long id, boolean available, List<Item> assignedItems) {
        this.id = id;
        this.available = available;
        this.assignedItems = assignedItems;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public List<Item> getAssignedItems() {
        return assignedItems;
    }

    public void setAssignedItems(List<Item> assignedItems) {
        this.assignedItems = assignedItems;
    }
}
