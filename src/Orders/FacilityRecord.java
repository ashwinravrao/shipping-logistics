/*
 * Developed by Ashwin Rao in June 2017.
 * Last Modified 9/7/18 12:43 PM.
 * Copyright (c) 2018. All rights reserved.
 */

package Orders;


import Exceptions.FacilityRecordException;
import java.util.ArrayList;


public class FacilityRecord implements FacilityRecordInterface {

    private String facilityName;
    private String itemId;
    private Order order;
    private int orderNumber;
    private int quantityRequested;
    private int inventory;
    private int itemsToDeduct;
    private int inventoryPostDeduction;
    private double processingEndDay;
    private double travelTime;
    private double arrivalDay;
    private ArrayList<Integer> modifiedSchedule;
    private double itemCost;
    private double facilityProcessingCost;
    private double totalCost;


    public FacilityRecord(Order order, int orderNumber, String facilityName, double facilityProcessingCost, String itemId, double itemCost, int quantityRequested, int inventory, int itemsToDeduct, int inventoryPostDeduction, double processingEndDay, double travelTime, double arrivalDay, ArrayList<Integer> modifiedSchedule) {
        try {
            this.order = order;
            setOrderNumber(orderNumber);
            setFacilityName(facilityName);
            setFacilityProcessingCost(facilityProcessingCost);
            setItemId(itemId);
            setItemCost(itemCost);
            setQuantityRequested(quantityRequested);
            setInventory(inventory);
            setItemsToDeduct(itemsToDeduct);
            setInventoryPostDeduction(inventoryPostDeduction);
            setProcessingEndDay(processingEndDay);
            setTravelTime(travelTime);
            setArrivalDay(arrivalDay);
            setModifiedSchedule(modifiedSchedule);
            calculateCosts();
        } catch (FacilityRecordException e) {
            System.err.println(e.getMessage());
        }
    }

    private void calculateCosts() {
        double transportCost = (Math.ceil(getTravelTime())) * 500;
        double processingCost = (this.processingEndDay) * facilityProcessingCost;
        double itemsCost = itemCost * itemsToDeduct;
        this.totalCost = transportCost + processingCost + itemsCost;
    }

    @Override
    public void setFacilityProcessingCost(double facilityProcessingCost) throws FacilityRecordException {
        if (facilityProcessingCost >= 0) {
            this.facilityProcessingCost = facilityProcessingCost;
        } else throw new FacilityRecordException("facility processing cost");
    }

    @Override
    public void setItemCost(double itemCost) throws FacilityRecordException {
        if (itemCost >= 0.0) {
            this.itemCost = itemCost;
        } else throw new FacilityRecordException("setting item cost");
    }

    @Override
    public void setOrderNumber(int orderNumber) throws FacilityRecordException {
        if (orderNumber >= 0) {
            this.orderNumber = orderNumber;
        } else throw new FacilityRecordException("setting order number");
    }

    @Override
    public void setFacilityName(String facilityName) throws FacilityRecordException {
        if (!facilityName.isEmpty()) {
            this.facilityName = facilityName;
        } else throw new FacilityRecordException("setting facility name");
    }

    @Override
    public void setItemId(String itemId) throws FacilityRecordException {
        if (!itemId.isEmpty()) {
            this.itemId = itemId;
        } else throw new FacilityRecordException("setting itemId");
    }

    @Override
    public void setQuantityRequested(int quantityRequested) throws FacilityRecordException {
        if (quantityRequested >= 0) {
            this.quantityRequested = quantityRequested;
        } else throw new FacilityRecordException("setting quantity requested");
    }

    @Override
    public void setInventory(int inventory) throws FacilityRecordException {
        if (inventory >= 0) {
            this.inventory = inventory;
        } else throw new FacilityRecordException("setting inventory");
    }

    @Override
    public void setItemsToDeduct(int itemsToDeduct) throws FacilityRecordException {
        if (itemsToDeduct >= 0) {
            this.itemsToDeduct = itemsToDeduct;
        } else throw new FacilityRecordException("setting deductible");

    }

    @Override
    public void setInventoryPostDeduction(int inventoryPostDeduction) throws FacilityRecordException {
        if (inventoryPostDeduction <= this.inventory) {
            this.inventoryPostDeduction = inventoryPostDeduction;
        } else throw new FacilityRecordException("setting inventory post-deduction");
    }

    @Override
    public void setProcessingEndDay(double processingEndDay) throws FacilityRecordException {
        if (processingEndDay > 0) {
            this.processingEndDay = processingEndDay;
        } else throw new FacilityRecordException("setting processing end day");
    }

    @Override
    public void setTravelTime(double travelTime) throws FacilityRecordException {
        if (travelTime > 0.0) {
            this.travelTime = travelTime;
        } else throw new FacilityRecordException("setting travel time");

    }

    @Override
    public void setArrivalDay(double arrivalDay) throws FacilityRecordException {
        if (arrivalDay > travelTime) {
            this.arrivalDay = arrivalDay;
        } else throw new FacilityRecordException("setting arrival day");
    }

    @Override
    public void setModifiedSchedule(ArrayList<Integer> modifiedSchedule) throws FacilityRecordException {
        if (!modifiedSchedule.isEmpty()) {
            this.modifiedSchedule = new ArrayList<>(modifiedSchedule);
        } else throw new FacilityRecordException("Modified schedule");
    }

    @Override
    public String getFacilityName() {
        return this.facilityName;
    }

    @Override
    public String getItemId() {
        return this.itemId;
    }

    @Override
    public int getQuantityRequested() {
        return this.quantityRequested;
    }

    @Override
    public int getInventory() {
        return this.inventory;
    }

    @Override
    public int getItemsToDeduct() {
        return this.itemsToDeduct;
    }

    @Override
    public int getInventoryPostDeduction() {
        return this.inventoryPostDeduction;
    }

    @Override
    public double getProcessingEndDay() {
        return this.processingEndDay;
    }

    @Override
    public double getTravelTime() {
        return this.travelTime;
    }

    @Override
    public double getArrivalDay() {
        return this.arrivalDay;
    }

    @Override
    public ArrayList<Integer> getModifiedSchedule() {
        return this.modifiedSchedule;
    }

    @Override
    public Order getOrder() {
        return this.order;
    }

    @Override
    public int getOrderNumber() {
        return this.orderNumber;
    }

    @Override
    public double getTotalCost() {
        return this.totalCost;
    }
}
