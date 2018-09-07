/*
 * Developed by Ashwin Rao in June 2017.
 * Last Modified 9/7/18 12:43 PM.
 * Copyright (c) 2018. All rights reserved.
 */

package Testing;

import Orders.OrderManager;

public class Main {

    public static void main(String[] args) {
        try {
            OrderManager oMgr = OrderManager.getInstance("src/Facilities/Network.xml", "src/Catalog/Catalog.xml", "src/Orders/Orders.xml", 50.0, 8.0);
            oMgr.facilityStatusOutput();
            oMgr.processOrders();
            oMgr.facilityStatusOutput();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

}
