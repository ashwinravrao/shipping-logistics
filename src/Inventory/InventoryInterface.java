/*
 * Developed by Ashwin Rao in June 2017.
 * Last Modified 9/7/18 12:43 PM.
 * Copyright (c) 2018. All rights reserved.
 */

package Inventory;


import Exceptions.BrokenLoaderFieldsException;
import java.util.HashMap;

public interface InventoryInterface {

    HashMap<String, String> getInventory();
    void setInventory(HashMap<String, String> inventoryMap) throws BrokenLoaderFieldsException;
    void printInventory();
    void updateInventory(String itemId, String itemQuantity);

}
