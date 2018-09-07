/*
 * Developed by Ashwin Rao in June 2017.
 * Last Modified 9/7/18 12:43 PM.
 * Copyright (c) 2018. All rights reserved.
 */

package Catalog;


import Exceptions.BrokenLoaderFieldsException;
import Exceptions.PathNotFoundException;
import Orders.OrderManager;
import java.util.Iterator;
import java.util.Map;

public final class CatalogManager {

    private static CatalogManager instance;
    private CatalogLoader catalogLoader;
    private boolean fileFound = true;
    static boolean catalogLoaded = false;      // property of any catalogManager object in that it indicates to other objects in Main that an instance of catalogManager has been created

    public static CatalogManager getInstance(String filePath) {
        if (instance == null) {
            instance = new CatalogManager(filePath);
        }
        return instance;
    }

    private CatalogManager(String filePath) {
        try {
            setFilePath(filePath);
            catalogLoaded = true;
        } catch (PathNotFoundException | BrokenLoaderFieldsException e) {
            fileFound = false;
            System.err.println("The catalog was not loaded because " + e.getMessage());
        }
    }

    private void setFilePath(String filePath) throws PathNotFoundException, BrokenLoaderFieldsException {
        if (!filePath.isEmpty()) {
            catalogLoader = new CatalogLoader(filePath);
        } else throw new PathNotFoundException(filePath);
    }

    public void printItemCatalog() {

        if (OrderManager.fileFound) {
            String beginCatalog = "--- BEGIN ITEM CATALOG ---";
            String endCatalog = "--- END ITEM CATALOG ---";
            System.out.println("------------------------------------------------------------------------------------------------------------------------");
            System.out.printf("\n%80s\n", beginCatalog);
            Iterator iter = catalogLoader.getCatalog().getItemCatalog().entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                System.out.printf("Item:   %s\nPrice:  $%.2f\n\n", entry.getKey().toString(), Double.parseDouble(entry.getValue().toString()));
                iter.remove();
            }
            System.out.printf("%80s\n", endCatalog);
            System.out.println("\n------------------------------------------------------------------------------------------------------------------------");
        }
    }

    public boolean isItemInCatalog(String item) {
        return catalogLoader.getCatalog().getItemCatalog().keySet().contains(item);
    }

    public double getItemCost(String itemId) {
        double itemCost = -1.0;
        Iterator iterator = catalogLoader.getCatalog().getItemCatalog().entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            if (entry.getKey().toString().equalsIgnoreCase(itemId)) {
                itemCost = Double.parseDouble(entry.getValue().toString());
            }
        }
        return itemCost;
    }

}
