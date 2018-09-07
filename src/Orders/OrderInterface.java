/*
 * Developed by Ashwin Rao in June 2017.
 * Last Modified 9/7/18 12:43 PM.
 * Copyright (c) 2018. All rights reserved.
 */

package Orders;


import Exceptions.BrokenLoaderFieldsException;

import java.util.HashMap;

public interface OrderInterface {

    void setOrderId(String orderId) throws BrokenLoaderFieldsException;
    String getOrderId();
    void setOrderTime(int orderTime)throws BrokenLoaderFieldsException;
    int getOrderTime();
    void setOrderDestination(String destination) throws BrokenLoaderFieldsException;
    String getDestination();
    void setOrderItems(HashMap<String, String> orderItemMap) throws BrokenLoaderFieldsException;
    HashMap<String, String> getOrderItems();


}
