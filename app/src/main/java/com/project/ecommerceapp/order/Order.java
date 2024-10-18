package com.project.ecommerceapp.order;

import java.util.ArrayList;

public class Order {

    private final String id;
    private final String address;
    private final String date;
    private final String status;
    private final String vendorId;
    private final ArrayList<OrderItem> orderItems = new ArrayList<>();

    public Order(String id, String address, String date, String status, String vendorId, ArrayList<OrderItem> orderItems) {
        this.id = id;
        this.address = address;
        this.date = date;
        this.status = status;
        this.vendorId = vendorId;

        this.orderItems.clear();
        this.orderItems.addAll(orderItems);
    }

    public String getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }

    public String getDate() {
        return date;
    }

    public String getStatus() {
        return status;
    }

    public String getVendorId() {
        return vendorId;
    }

    public ArrayList<OrderItem> getOrderItems() {
        return orderItems;
    }

}