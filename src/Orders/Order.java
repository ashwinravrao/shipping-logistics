/*
 * Developed by Ashwin Rao in June 2017.
 * Last Modified 9/7/18 12:43 PM.
 * Copyright (c) 2018. All rights reserved.
 */

package Orders;


import Exceptions.BrokenLoaderFieldsException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Order implements OrderInterface {

    private String orderId;
    private int orderTime;
    private String destination;
    private HashMap<String, String> orderItemMap = new HashMap<>();

    public Order(String orderId, int orderTime, String destination, HashMap<String, String> orderItemMap) throws BrokenLoaderFieldsException {
        setOrderId(orderId);
        setOrderTime(orderTime);
        setOrderDestination(destination);
        setOrderItems(orderItemMap);
    }

    @Override
    public HashMap<String, String> getOrderItems() {
        return orderItemMap;
    }

    @Override
    public void setOrderItems(HashMap<String, String> orderItemMap) throws BrokenLoaderFieldsException {
        if (!orderItemMap.isEmpty()) {
            this.orderItemMap = orderItemMap;
        } else throw new BrokenLoaderFieldsException("HashMap<String, String> orderItemMap");
    }

    @Override
    public String getOrderId() {
        return orderId;
    }

    @Override
    public void setOrderId(String orderId) throws BrokenLoaderFieldsException {
        if (!orderId.isEmpty()) {
            this.orderId = orderId;
        } else throw new BrokenLoaderFieldsException("String orderId");
    }

    @Override
    public int getOrderTime() {
        return orderTime;
    }

    @Override
    public void setOrderTime(int orderTime) throws BrokenLoaderFieldsException {
        if (orderTime > 0) {
            this.orderTime = orderTime;
        } else throw new BrokenLoaderFieldsException("int orderTime");
    }

    @Override
    public String getDestination() {
        return destination;
    }

    @Override
    public void setOrderDestination(String destination) throws BrokenLoaderFieldsException {
        if (!destination.isEmpty()) {
            this.destination = destination;
        } else throw new BrokenLoaderFieldsException("String destination");
    }

}
