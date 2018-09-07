/*
 * Developed by Ashwin Rao in June 2017.
 * Last Modified 9/7/18 12:43 PM.
 * Copyright (c) 2018. All rights reserved.
 */

package Catalog;


import Exceptions.BrokenLoaderFieldsException;
import java.util.HashMap;


public class Catalog implements CatalogInterface {

    private HashMap<String, String> itemCatalog = new HashMap<>();


    public Catalog(HashMap<String, String> itemCatalog) throws BrokenLoaderFieldsException {
        setItemCatalog(itemCatalog);
    }

    @Override
    public void setItemCatalog(HashMap<String, String> itemCatalog) throws BrokenLoaderFieldsException {
        if (!itemCatalog.isEmpty()) {
            this.itemCatalog = itemCatalog;
        } else throw new BrokenLoaderFieldsException("HashMap<String, String> itemCatalog");
    }

    @Override
    public HashMap<String, String> getItemCatalog() {
        return itemCatalog;
    }

}
