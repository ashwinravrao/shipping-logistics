/*
 * Developed by Ashwin Rao in June 2017.
 * Last Modified 9/7/18 12:43 PM.
 * Copyright (c) 2018. All rights reserved.
 */

package Exceptions;


public class UnknownFacilityTypeException extends Exception {

    public UnknownFacilityTypeException(String facilityType) {
        super("the facility type: " + facilityType + " has no corresponding object at this time");
    }
}
