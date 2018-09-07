/*
 * Developed by Ashwin Rao in June 2017.
 * Last Modified 9/7/18 12:43 PM.
 * Copyright (c) 2018. All rights reserved.
 */

package Exceptions;


public class BrokenLoaderFieldsException extends Exception {

    public BrokenLoaderFieldsException(String brokenFields) {
        super("There was a problem assigning the following field(s): " + brokenFields);
    }
}
