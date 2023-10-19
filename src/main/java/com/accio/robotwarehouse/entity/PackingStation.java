package com.accio.robotwarehouse.entity;

import javax.persistence.*;

@Entity
@Table(name = "packing_stations")
public class PackingStation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "station_id")
    private long id;

    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;

    public PackingStation() {
    }

    public PackingStation(long id, Order order) {
        this.id = id;
        this.order = order;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
