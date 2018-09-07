/*
 * Developed by Ashwin Rao in June 2017.
 * Last Modified 9/7/18 12:43 PM.
 * Copyright (c) 2018. All rights reserved.
 */

package Orders;


import Exceptions.FacilityRecordException;
import java.util.ArrayList;

public interface FacilityRecordInterface {

    void setFacilityName(String facilityName) throws FacilityRecordException;
    void setOrderNumber(int orderNumber) throws FacilityRecordException;
    void setItemId(String itemId) throws FacilityRecordException;
    void setQuantityRequested(int quantityRequested) throws FacilityRecordException;
    void setInventory(int inventory) throws FacilityRecordException;
    void setItemsToDeduct(int itemsToDeduct) throws FacilityRecordException;
    void setInventoryPostDeduction(int inventoryPostDeduction) throws FacilityRecordException;
    void setProcessingEndDay(double processingEndDay) throws FacilityRecordException;
    void setTravelTime(double travelTime) throws FacilityRecordException;
    void setArrivalDay(double arrivalDay) throws FacilityRecordException;
    void setModifiedSchedule(ArrayList<Integer> modifiedSchedule) throws FacilityRecordException;
    void setFacilityProcessingCost(double facilityProcessingCost) throws FacilityRecordException;
    void setItemCost(double itemCost) throws FacilityRecordException;

    double getTotalCost();
    String getFacilityName();
    String getItemId();
    int getQuantityRequested();
    int getInventory();
    int getItemsToDeduct();
    int getInventoryPostDeduction();
    double getProcessingEndDay();
    double getTravelTime();
    double getArrivalDay();
    int getOrderNumber();
    Order getOrder();
    ArrayList<Integer> getModifiedSchedule();
}
