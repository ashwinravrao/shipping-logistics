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


public interface FacilityInterface {

    String getWarehouseName();
    int getpRate();
    double getpCost();
    HashMap<String, String> getLinkMap();
    Inventory getInventoryObj();
    Schedule getScheduleObj();

    void setWarehouseName(String warehouseName) throws BrokenLoaderFieldsException;
    void setpRate(int pRate) throws BrokenLoaderFieldsException;
    void setpCost(double pCost) throws BrokenLoaderFieldsException;
    void setLinkMap(HashMap<String, String> linkMap) throws BrokenLoaderFieldsException;
    void printLinkMap();
}
