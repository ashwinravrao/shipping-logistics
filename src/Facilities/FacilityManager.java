/*
 * Developed by Ashwin Rao in June 2017.
 * Last Modified 9/7/18 12:43 PM.
 * Copyright (c) 2018. All rights reserved.
 */

package Facilities;


import Exceptions.BrokenLoaderFieldsException;
import Exceptions.PathNotFoundException;
import Exceptions.UnknownFacilityTypeException;
import Exceptions.UpdateFailedException;
import Orders.OrderManager;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;


public final class FacilityManager {

    private static FacilityManager instance;
    private boolean fileFound = true;
    private FacilityLoader facilityLoader;


    public static FacilityManager getInstance(String filePath) {
        if (instance == null) {
            instance = new FacilityManager(filePath);
        }
        return instance;
    }

    private FacilityManager(String filePath) {
        try {
            setFilePath(filePath);
        } catch (PathNotFoundException | BrokenLoaderFieldsException | UnknownFacilityTypeException e) {
            fileFound = false;
            System.err.println("The network, inventory and schedule were not loaded because " + e.getMessage());
        }
    }

    private void setFilePath(String filePath) throws PathNotFoundException, BrokenLoaderFieldsException, UnknownFacilityTypeException {
        if (!filePath.isEmpty()) {
            facilityLoader = new FacilityLoader(filePath);
        } else throw new PathNotFoundException(filePath);
    }

    public ArrayList<FacilityInterface> getFacilityObjects() {
        return facilityLoader.getFacilities();
    }

    public int getFacilityInventoryForRequestedItem(String facilityName, String requestedItem) {
        int deliverableQuantity = 0;
        for (FacilityInterface f : facilityLoader.getFacilities()) {
            if (f.getWarehouseName().equalsIgnoreCase(facilityName)) {
                Iterator iterator = f.getInventoryObj().getInventory().entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry entry = (Map.Entry) iterator.next();
                    if (entry.getKey().toString().equals(requestedItem)) {
                        deliverableQuantity = Integer.parseInt(entry.getValue().toString());
                    }
                }
            }
        }
        if (deliverableQuantity == 0) {
            System.err.println("There is no availability of the requested item at the specified facility");
        }

        return deliverableQuantity;
    }

    public void facilityStatusOutput() {

        if (OrderManager.fileFound) {
            String beginReport = "************ START FACILITY STATUS REPORT ***************** START FACILITY STATUS REPORT ***************** START FACILITY STATUS REPORT ***************** START FACILITY STATUS REPORT ***************** START FACILITY STATUS REPORT ***************** START FACILITY STATUS REPORT **********";
            String endReport = "************** END FACILITY STATUS REPORT ******************* END FACILITY STATUS REPORT ******************* END FACILITY STATUS REPORT ******************* END FACILITY STATUS REPORT ******************* END FACILITY STATUS REPORT ******************* END FACILITY STATUS REPORT ************";

            System.out.printf("\n\n\n\n\n\n" + beginReport + "\n\n");
            for (FacilityInterface w : facilityLoader.getFacilities()) {
                System.out.println(w.getWarehouseName());
                System.out.println(w.getClass().toString().replace("class Facilities.", "").trim());
                for (int i = 0; i < w.getWarehouseName().length() + 1; i++) {
                    System.out.print("-");
                }
                System.out.println("\nRate per Day: " + w.getpRate());
                System.out.println("Cost per Day: " + w.getpCost() + "\n");
                System.out.println("Direct Links:");
                w.printLinkMap();
                w.getInventoryObj().printInventory();
                w.getScheduleObj().printSchedule();
            }
            System.out.printf("\n\n" + endReport + "\n\n\n\n\n\n");
        }
    }

    public void updateFacilityInventory(String facilityName, String itemId, String newItemQuantity) throws UpdateFailedException {

        boolean updateSucceeded = false;

        for (FacilityInterface f : facilityLoader.getFacilities()) {
            if (f.getWarehouseName().equalsIgnoreCase(facilityName)) {
                Iterator iterator = f.getInventoryObj().getInventory().entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry entry = (Map.Entry) iterator.next();
                    if (entry.getKey().toString().equals(itemId)) {
                        entry.setValue(newItemQuantity);
                        updateSucceeded = true;
                    }
                }
            }
        }
        if (!updateSucceeded) throw new UpdateFailedException("Updating inventory for " + facilityName + " failed");
    }

    public ArrayList<String> facilitiesWithItemInInventory(String destination, String itemId) {
        ArrayList<String> identifiedFacilities = new ArrayList<>();
        for (FacilityInterface f : facilityLoader.getFacilities()) {
            Iterator iterator = f.getInventoryObj().getInventory().entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();
                if (entry.getKey().toString().equalsIgnoreCase(itemId) && (!entry.getValue().toString().equalsIgnoreCase("0")) && (!f.getWarehouseName().equalsIgnoreCase(destination))) {
                    identifiedFacilities.add(f.getWarehouseName());
                }
            }
        }
        return identifiedFacilities;
    }

    public void updateSchedule(String facilityName, ArrayList<Integer> schedule) throws UpdateFailedException {
        boolean updateSucceeded = false;
        for (FacilityInterface f : facilityLoader.getFacilities()) {
            if (f.getWarehouseName().equalsIgnoreCase(facilityName)) {
                f.getScheduleObj().updateSchedule(facilityName, schedule);
                updateSucceeded = true;
            }
        }
        if (!updateSucceeded) throw new UpdateFailedException("The schedule for " + facilityName + " failed to update");
    }

    public double getFacilityProcessingCost(String facilityName) {
        double processingCost = 0.0;
        for (FacilityInterface f : facilityLoader.getFacilities()) {
            if (f.getWarehouseName().equalsIgnoreCase(facilityName)) {
                processingCost = f.getpCost();
            }
        }
        return processingCost;
    }

}
