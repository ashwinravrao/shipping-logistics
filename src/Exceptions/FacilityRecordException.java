/*
 * Developed by Ashwin Rao in June 2017.
 * Last Modified 9/7/18 12:43 PM.
 * Copyright (c) 2018. All rights reserved.
 */

package Exceptions;

public class FacilityRecordException extends Exception {

    public FacilityRecordException() {
        super("There was a problem creating a facility record");
    }

    public FacilityRecordException(String message) {
        super(message);
    }
}
