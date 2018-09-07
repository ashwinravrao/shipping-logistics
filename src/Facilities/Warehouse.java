/*
 * Developed by Ashwin Rao in June 2017.
 * Last Modified 9/7/18 12:43 PM.
 * Copyright (c) 2018. All rights reserved.
 */

package Facilities;

import Exceptions.BrokenLoaderFieldsException;
import Inventory.Inventory;
import Schedule.Schedule;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class Warehouse implements FacilityInterface {

    private Inventory inventoryObj;
    private Schedule scheduleObj;

    private String warehouseName;
    private int pRate;
    private double pCost;
    private HashMap<String, String> linkMap = new HashMap<>();

    public Warehouse(String warehouseName, int pRate, double pCost, HashMap<String, String> linkMap, HashMap<String, String> inventoryMap) throws BrokenLoaderFieldsException {
        setWarehouseName(warehouseName);
        setpRate(pRate);
        setpCost(pCost);
        setLinkMap(linkMap);
        scheduleObj = new Schedule(pRate);              // error checking handled by object's constructor
        inventoryObj = new Inventory(inventoryMap);     // ''
    }

    @Override
    public Schedule getScheduleObj() {
        return scheduleObj;
    }

    @Override
    public Inventory getInventoryObj() {
        return inventoryObj;
    }

    @Override
    public String getWarehouseName() {
        return warehouseName;
    }

    @Override
    public void setWarehouseName(String warehouseName) throws BrokenLoaderFieldsException {
        if (warehouseName.length() > 0) {
            this.warehouseName = warehouseName;
        } else throw new BrokenLoaderFieldsException("String warehouseName");
    }

    @Override
    public int getpRate() {
        return pRate;
    }

    @Override
    public void setpRate(int pRate) throws BrokenLoaderFieldsException {
        if (pRate >= 0) {
            this.pRate = pRate;
        } else throw new BrokenLoaderFieldsException("int pRate");
    }

    @Override
    public double getpCost() {
        return pCost;
    }

    @Override
    public void setpCost(double pCost) throws BrokenLoaderFieldsException {
        if (pCost >= 0.0) {
            this.pCost = pCost;
        } else throw new BrokenLoaderFieldsException("double pCost");
    }

    @Override
    public HashMap<String, String> getLinkMap() {
        return linkMap;
    }

    @Override
    public void setLinkMap(HashMap<String, String> linkMap) throws BrokenLoaderFieldsException {
        if (!linkMap.isEmpty()) {
            this.linkMap = linkMap;
        } else throw new BrokenLoaderFieldsException("HashMap<String, String> linkMap");
    }

    @Override
    public void printLinkMap() {
        Iterator iter = this.getLinkMap().entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            String[] keyPreProcessing = entry.getKey().toString().split(" : ");
            String keyPostProcessing = keyPreProcessing[1];
            double miles = Double.parseDouble(entry.getValue().toString());
            double days = (miles / (8.0 * 50.0));
            /* ^ hard-coding used solely for estimating distance property in the Facility Status Output, not for determining shortest path distance */
            System.out.printf("%s (%.1fd); ", keyPostProcessing, days);
        }
    }
}
