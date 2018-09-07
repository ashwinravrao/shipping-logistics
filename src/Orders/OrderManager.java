/*
 * Developed by Ashwin Rao in June 2017.
 * Last Modified 9/7/18 12:43 PM.
 * Copyright (c) 2018. All rights reserved.
 */

package Orders;


import Catalog.CatalogManager;
import Exceptions.*;
import Facilities.FacilityInterface;
import Facilities.FacilityManager;
import ShortestPath.ShortestPath;

import java.util.*;


public final class OrderManager {

    private int orderNumber = 1;
    private static OrderManager instance;
    public OrderLoader loader;                                                                                             // loader holds raw data, making it public would turn its get methods loose and spill raw data to the user
    public FacilityManager fMgr;                                                                                            // facades can be public since they are selective with information they release & if want to make private would have to have parallel methods in OrderManager for every public method in both facades
    public CatalogManager cMgr;
    private OrderSolution solution = new OrderSolution();
    private ShortestPath shortestPath;
    public static boolean fileFound = false;                                                                                // statically accessible flag which tells other program objects whether all necessary data has been loaded
    private double averageMilesPerHour;
    private double drivingHoursPerDay;
    private ArrayList<Order> localOrdersList;
    ArrayList<FacilityRecord> recordsList = new ArrayList<>();
    private ArrayList<FacilityRecord> orderSolutions = new ArrayList<>();

    public static OrderManager getInstance(String networkFilePath, String catalogFilePath, String orderFilePath, double averageMilesPerHour, double drivingHoursPerDay) throws PathNotFoundException, BrokenLoaderFieldsException, MaxSpeedException, DriveTimeException {
        if (instance == null) {
            instance = new OrderManager(networkFilePath, catalogFilePath, orderFilePath, averageMilesPerHour, drivingHoursPerDay);
        }
        return instance;
    }

    private OrderManager(String networkFilePath, String catalogFilePath, String orderFilePath, double averageMilesPerHour, double drivingHoursPerDay) throws PathNotFoundException, BrokenLoaderFieldsException, MaxSpeedException, DriveTimeException {
        createAllInstances(networkFilePath, catalogFilePath, orderFilePath);
        setDrivingParams(averageMilesPerHour, drivingHoursPerDay);
        localOrdersList = new ArrayList<>(loader.getOrdersList());
    }

    private void setDrivingParams(double averageMilesPerHour, double drivingHoursPerDay) throws MaxSpeedException, DriveTimeException {
        if ((averageMilesPerHour <= 85.0) && (averageMilesPerHour > 0.0)) {
            this.averageMilesPerHour = averageMilesPerHour;
        } else {
            throw new MaxSpeedException();
        }

        if ((drivingHoursPerDay <= 14.0) && (drivingHoursPerDay > 0.0)) {
            this.drivingHoursPerDay = drivingHoursPerDay;
        } else {
            throw new DriveTimeException();
        }
    }

    private void createAllInstances(String networkFilePath, String catalogFilePath, String orderFilePath) throws PathNotFoundException, BrokenLoaderFieldsException {
        if (networkFilePath.equals("src/Facilities/Network.xml")) {
            fMgr = FacilityManager.getInstance(networkFilePath);
        } else throw new PathNotFoundException(networkFilePath);
        if (catalogFilePath.equals("src/Catalog/Catalog.xml")) {
            cMgr = CatalogManager.getInstance(catalogFilePath);
        } else throw new PathNotFoundException(catalogFilePath);
        if (orderFilePath.equals("src/Orders/Orders.xml")) {
            loader = new OrderLoader(orderFilePath);
        } else throw new PathNotFoundException(orderFilePath);

        fileFound = true;
    }

    public void printOrders() {
        if (fileFound) {
            String beginReport = "--- BEGIN ORDER REPORT ---";
            String endReport = "--- END ORDER REPORT ---";

            System.out.printf("\n%80s\n\n", beginReport);
            int orderNumber = 1;
            for (Order o : localOrdersList) {
                System.out.println("Order #" + orderNumber + "\n");
                System.out.printf("   Order Id:             %s", o.getOrderId());
                System.out.printf("\n   Order Time:           %s", o.getOrderTime());
                System.out.printf("\n   Order Destination:    %s", o.getDestination());
                System.out.println("\n   List of Order Items:\n");
                int itemNumber = 1;
                for (Object o1 : o.getOrderItems().entrySet()) {
                    Map.Entry entry = (Map.Entry) o1;
                    System.out.println("   " + itemNumber + ")  Item Id:   " + entry.getKey() + ",     Quantity:   " + entry.getValue());
                    itemNumber++;
                }
                System.out.println();
                System.out.println("------------------------------------------------------------------------------------------------------------------------\n");
                orderNumber++;
            }
            System.out.printf("\n%80s\n\n", endReport);
        }
    }

    private void printFacilityRecords(ArrayList<FacilityRecord> records) {
        for (FacilityRecord f : records) {
            Double pED = f.getProcessingEndDay();
            Double arrDay = f.getArrivalDay();
            Double travelTime = f.getTravelTime();

            System.out.println("Facility:                                 " + f.getFacilityName());
            System.out.println("Item Requested:                           " + f.getItemId());
            System.out.println("Quantity Requested:                       " + f.getQuantityRequested());
            System.out.println("Inventory @ Facility:                     " + f.getInventory());
            System.out.println("Number of Items (to take from facility):  " + f.getItemsToDeduct());
            System.out.println("Inventory Post-Deduction:                 " + f.getInventoryPostDeduction());
            System.out.println("Processing End Day:                       " + pED.intValue());
            System.out.println("Travel Time:                              " + f.getTravelTime());
            System.out.println("Arrival Day:                              " + arrDay.intValue() + " days");
            System.out.println("\nModified Schedule:\n");
            for (Integer i : f.getModifiedSchedule()) {
                System.out.print(i + "  ");
            }
            System.out.println("\n");
        }
    }

    public void processOrders() {
        try {
            for (Order o : localOrdersList) {
                process(o);
            }
        } catch (OrderProcessingException e) {
            System.err.println(e.getMessage());
        }
    }

    private void process(Order o) throws OrderProcessingException {

        String requestedItem;
        int requestedQuantity;
        ArrayList<String> facilitiesWithInventory;
        solution.printHeader(orderNumber, o);

        while (!notOrderFilled(o)) {
            Iterator iterator = o.getOrderItems().entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();
                if (!entry.getValue().toString().equalsIgnoreCase("0")) {
                    requestedItem = entry.getKey().toString();
                    requestedQuantity = Integer.parseInt(entry.getValue().toString());

                    if (cMgr.isItemInCatalog(requestedItem)) {
                        facilitiesWithInventory = new ArrayList<>(fMgr.facilitiesWithItemInInventory(o.getDestination(), requestedItem));
                    } else
                        throw new OrderProcessingException("The item " + requestedItem + " was rejected from an order since it is not a valid item in the catalog");
                    recordsList = compileFacilityRecords(o, facilitiesWithInventory, requestedItem, requestedQuantity);
                    orderSolutions.add(recordsList.get(0));
                    processTopFacilityRecord(o.getOrderId(), orderSolutions);


//                    // prints facility records for testing
//                    System.out.println("-----------------------------------------------------------------");
//                    System.out.print("Order ID: " + o.getOrderId() + ", Item #" + counter + "\n\n");
//                    printFacilityRecords(recordsList);

                }
            }
        }
        solution.printProcessingSolution(o, orderSolutions);
        orderNumber++;
    }

    private boolean notOrderFilled(Order o) {
        boolean notOrderFilled = true;
        for (String s : o.getOrderItems().values()) {
            if (!s.equalsIgnoreCase("0")) {
                notOrderFilled = false;
            }
        }
        return notOrderFilled;
    }

    private ArrayList<FacilityRecord> compileFacilityRecords(Order o, ArrayList<String> facilitiesWithInventory, String requestedItem, int requestedQuantity) throws OrderProcessingException {

        ArrayList<FacilityRecord> records = new ArrayList<>();

        if (!facilitiesWithInventory.isEmpty()) {
            for (String currentFacility : facilitiesWithInventory) {
                int currentFacilityInventory = fMgr.getFacilityInventoryForRequestedItem(currentFacility, requestedItem);
                double processingCost = fMgr.getFacilityProcessingCost(currentFacility);
                int itemsToDeduct = 0;
                if (requestedQuantity > currentFacilityInventory) {
                    itemsToDeduct = currentFacilityInventory;
                }
                if (requestedQuantity < currentFacilityInventory) {
                    itemsToDeduct = requestedQuantity;
                }
                double travelTime = travelTimeCalculator(currentFacility, o.getDestination());
                double processingEndDay = Double.parseDouble(processSchedule(currentFacility, currentFacilityInventory, requestedQuantity).get(1).get(0).toString());
                double arrivalDay = Math.ceil(travelTime + processingEndDay);
                ArrayList<Integer> modifiedSchedule = new ArrayList<>(processSchedule(currentFacility, currentFacilityInventory, requestedQuantity).get(0));
                int inventoryPostDeduction = currentFacilityInventory - itemsToDeduct;
                records.add(new FacilityRecord(o, orderNumber, currentFacility, processingCost, requestedItem, cMgr.getItemCost(requestedItem), requestedQuantity, currentFacilityInventory, itemsToDeduct, inventoryPostDeduction, processingEndDay, travelTime, arrivalDay, modifiedSchedule));
            }

            Collections.sort(records, Comparator.comparingDouble(FacilityRecord::getArrivalDay));   // sort in place

        } else
            throw new OrderProcessingException("There was a problem compiling the list of facility records during order processing");
        return records;
    }

    private double travelTimeCalculator(String currentFacility, String destination) throws OrderProcessingException {

        double travelTime = 0.0;
        shortestPath = new ShortestPath(currentFacility, destination, averageMilesPerHour, drivingHoursPerDay, fMgr.getFacilityObjects());

        for (Object o : shortestPath.getMapLowDays().entrySet()) {
            Map.Entry entry = (Map.Entry) o;
            String[] split = entry.getValue().toString().split(" : ");
            travelTime = Double.parseDouble(split[1]);
        }
        if (travelTime != 0.0)
            return travelTime;
        else throw new OrderProcessingException("There was a problem calculating the travel time for an item");
    }

    private ArrayList<ArrayList<Integer>> processSchedule(String facilityName, int inventoryAtFacility, int itemQuantityRequested) throws OrderProcessingException {      // set to private at release

        int processingDaysCounter = 0;
        int quantityRemaining;
        ArrayList<ArrayList<Integer>> oldScheduleCopy = new ArrayList<>();
        ArrayList<ArrayList<Integer>> methodResult = new ArrayList<>();
        ArrayList<Integer> availableCopy;
        ArrayList<Integer> modifiedAvailable = new ArrayList<>();
        ArrayList<Integer> processingDaysCounterList = new ArrayList<>();

        if (inventoryAtFacility > itemQuantityRequested) {
            quantityRemaining = itemQuantityRequested;
        } else {
            quantityRemaining = inventoryAtFacility;
        }

        for (FacilityInterface f : fMgr.getFacilityObjects()) {
            if (f.getWarehouseName().equalsIgnoreCase(facilityName)) {
                oldScheduleCopy = new ArrayList<>(f.getScheduleObj().getSchedule());                                        // gets the schedule for the current facility and saves it AS A COPY to oldScheduleCopy
            }
        }
        if (!oldScheduleCopy.isEmpty()) {                                                                               // makes sure the previous step actually did what it was supposed to
            availableCopy = new ArrayList<>(oldScheduleCopy.get(1));                                                    // saves a local COPY of the available-items-to-process arrayList (2nd half of the overall schedule ArrayList<ArrayList<Integer>>)
            for (int i = 0; i <= availableCopy.size() - 1; i++) {
                if (quantityRemaining > availableCopy.get(i)) {
                    quantityRemaining = quantityRemaining - availableCopy.get(i);
                    modifiedAvailable.add(i, 0);
                    processingDaysCounter++;
                } else if ((quantityRemaining > 0) && (quantityRemaining < availableCopy.get(i))) {
                    modifiedAvailable.add(i, (availableCopy.get(i) - quantityRemaining));
                    quantityRemaining = 0;
                    processingDaysCounter++;
                } else if (quantityRemaining == availableCopy.get(i)) {
                    modifiedAvailable.add(i, (availableCopy.get(i) - quantityRemaining));
                    quantityRemaining = 0;
                    processingDaysCounter++;
                } else if (quantityRemaining == 0) {
                    modifiedAvailable.add(i, availableCopy.get(i));
                }
            }
        } else throw new OrderProcessingException("There was an error processing the schedule for " + facilityName);

        processingDaysCounterList.add(processingDaysCounter);
        methodResult.add(modifiedAvailable);
        methodResult.add(processingDaysCounterList);

        return methodResult;
    }

    private void processTopFacilityRecord(String orderId, ArrayList<FacilityRecord> orderItemsSolutionList) {
        try {
            for (FacilityRecord f : orderItemsSolutionList) {
                fMgr.updateFacilityInventory(f.getFacilityName(), f.getItemId(), Integer.toString(f.getInventoryPostDeduction()));
                fMgr.updateSchedule(f.getFacilityName(), f.getModifiedSchedule());
                String newItemQuantity = Integer.toString((f.getQuantityRequested() - f.getItemsToDeduct()));

                for (Order o : localOrdersList) {
                    if (o.getOrderId().equalsIgnoreCase(orderId)) {
                        Iterator iterator = o.getOrderItems().entrySet().iterator();
                        while (iterator.hasNext()) {
                            Map.Entry entry = (Map.Entry) iterator.next();
                            if (entry.getKey().toString().equalsIgnoreCase(f.getItemId())) {
                                entry.setValue(newItemQuantity);
                            }
                        }
                    }
                }
            }
        } catch (UpdateFailedException e) {
            System.err.println(e.getMessage());
        }
    }

    public void facilityStatusOutput() {
        fMgr.facilityStatusOutput();
    }
}
