/*
 * Developed by Ashwin Rao in June 2017.
 * Last Modified 9/7/18 12:43 PM.
 * Copyright (c) 2018. All rights reserved.
 */

package Orders;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;


public class OrderSolution {


    public OrderSolution() {
    }


    public void printHeader(int orderNumber, Order order) {

        int itemCounter = 1;
        System.out.println("\n-------------------------------------------------------------------------------------\n");
        System.out.println("Order #" + orderNumber);
        System.out.println("  Order Id:      " + order.getOrderId());
        System.out.println("  Order Time:    Day " + order.getOrderTime());
        System.out.println("  Destination:   " + order.getDestination());
        System.out.println("\n  List of Order Items:\n");
        Iterator iterator = order.getOrderItems().entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            System.out.printf("%6d%s%-10s%-19s%-22s\n",itemCounter,")  Item ID:    ",entry.getKey().toString(),"//   Quantity:",entry.getValue().toString());
            itemCounter++;
        }
        System.out.println("\nProcessing Solution:");
    }

    public void printProcessingSolution(Order o, ArrayList<FacilityRecord> records) {

        double totalRunningCost = 0.0;

        Iterator iterator = o.getOrderItems().entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            System.out.println("\n" + entry.getKey().toString() + ":");
            System.out.printf("%6s%-22s%-22s%-22s%-22s","  ","   Facility","  Quantity","Cost"," Arrival Day");

            double runningCostLocal = 0.0;
            int runningQuantity = 0;
            int counter = 1;
            ArrayList<Integer> arrivals = new ArrayList<>();

            for (FacilityRecord f : records) {
                if (f.getItemId().equalsIgnoreCase(entry.getKey().toString()) && (f.getItemsToDeduct() > 0)) {
                    Double arrivalDay = f.getArrivalDay();
                    int arr = arrivalDay.intValue();
                    arrivals.add(arr);
                    System.out.printf("\n%6d%-22s%-22s$%,-22.2f%-22s",counter,")  " + f.getFacilityName(),"  " + f.getItemsToDeduct(),f.getTotalCost(),arr);

                    runningCostLocal += f.getTotalCost();
                    runningQuantity += f.getItemsToDeduct();
                    counter++;
                }
            }
            String min = Collections.min(arrivals).toString();
            String max = Collections.max(arrivals).toString();
            String totalArrivalsRange;
            if (min.equals(max)) {
                totalArrivalsRange = "[" + min + "]";
            } else {
                totalArrivalsRange = "[" + min + " - " + max + "]";
            }

            System.out.printf("\n%6s%-22s%-22s$%,-22.2f%-22s","  ","   TOTAL","  " + runningQuantity,runningCostLocal,totalArrivalsRange);
            totalRunningCost += runningCostLocal;
        }
        System.out.printf("\n\n%s%8s%,.2f\n","Total Cost:","$",totalRunningCost);
    }
}
