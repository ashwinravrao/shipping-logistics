/*
 * Developed by Ashwin Rao in June 2017.
 * Last Modified 9/7/18 12:43 PM.
 * Copyright (c) 2018. All rights reserved.
 */

package Exceptions;


public class IllegalStartEndException extends Exception {

    public IllegalStartEndException() {
        super("the start AND/OR end locations are not in the list of available facilities");
    }

    public IllegalStartEndException(String fields) {
        super(fields + " is not in the list of available facilities");
    }
}
