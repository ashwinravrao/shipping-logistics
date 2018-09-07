/*
 * Developed by Ashwin Rao in June 2017.
 * Last Modified 9/7/18 12:43 PM.
 * Copyright (c) 2018. All rights reserved.
 */

package Facilities;

import Exceptions.BrokenLoaderFieldsException;
import Exceptions.UnknownFacilityTypeException;
import java.util.HashMap;


public class FacilityFactory {

    public static FacilityInterface build(String facilityType, String warehouseName, int pRate, double pCost,
                                          HashMap<String, String> linkMap,
                                          HashMap<String, String> inventoryMap) throws BrokenLoaderFieldsException,
                                          UnknownFacilityTypeException {
        if (facilityType.equalsIgnoreCase("warehouse")) {
            return new Warehouse(warehouseName, pRate, pCost, linkMap, inventoryMap);
        } else throw new UnknownFacilityTypeException(facilityType);
    }
}
