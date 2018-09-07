/*
 * Developed by Ashwin Rao in June 2017.
 * Last Modified 9/7/18 12:43 PM.
 * Copyright (c) 2018. All rights reserved.
 */

package Inventory;

import Exceptions.BrokenLoaderFieldsException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class Inventory implements InventoryInterface {

    private HashMap<String, String> inventoryMap = new HashMap<>();

    public Inventory(HashMap<String, String> inventoryMap) throws BrokenLoaderFieldsException {
        setInventory(inventoryMap);
    }

    @Override
    public void setInventory(HashMap<String, String> inventoryMap) throws BrokenLoaderFieldsException {
        if (!inventoryMap.isEmpty()) {
            this.inventoryMap = inventoryMap;
        } else throw new BrokenLoaderFieldsException("HashMap<String, String> inventoryMap");
    }

    @Override
    public HashMap<String, String> getInventory() {
        return inventoryMap;
    }

    private void printInventoryMap(HashMap<String, String> inventoryMap) {

        System.out.printf("\n%14s", "Item ID");
        System.out.printf("%15s", "Quantity");
        System.out.println();

        Iterator iterator = inventoryMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            System.out.printf("       %-14s", entry.getKey().toString());
            System.out.printf("%s", entry.getValue().toString());
            System.out.println();
            iterator.remove();
        }
    }

    @Override
    public void printInventory() {

        boolean anyDepleted = false;
        HashMap<String, String> inventoryCopy = new HashMap<>(this.getInventory());
        HashMap<String, String> depleted = new HashMap<>();

        Iterator iterator = inventoryCopy.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            if (entry.getValue().toString().equals("0")) {
                depleted.put(entry.getKey().toString(), entry.getValue().toString());
                iterator.remove();
                anyDepleted = true;
            }
        }
        System.out.println("\n\nActive Inventory:");
        printInventoryMap(inventoryCopy);
        System.out.print("\nDepleted (Used-Up) Inventory: ");
        if (!anyDepleted) {
            System.out.println("None");
        } else {
            System.out.println();
            printInventoryMap(depleted);
        }
    }

    @Override
    public void updateInventory(String itemId, String newItemQuantity) {
        Iterator iterator = this.getInventory().entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            if (entry.getKey().toString().equalsIgnoreCase(itemId)) {
                entry.setValue(newItemQuantity);
            }
        }
    }

}
