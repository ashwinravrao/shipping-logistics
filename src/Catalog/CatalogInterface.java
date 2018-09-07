/*
 * Developed by Ashwin Rao in June 2017.
 * Last Modified 9/7/18 12:43 PM.
 * Copyright (c) 2018. All rights reserved.
 */

package Catalog;

import Exceptions.BrokenLoaderFieldsException;

import java.util.HashMap;


public interface CatalogInterface {

    HashMap<String, String> getItemCatalog();
    void setItemCatalog(HashMap<String, String> itemCatalog) throws BrokenLoaderFieldsException;
}
