/*
 * Developed by Ashwin Rao in June 2017.
 * Last Modified 9/7/18 12:43 PM.
 * Copyright (c) 2018. All rights reserved.
 */

package Exceptions;


public class PathNotFoundException extends Exception {

    public PathNotFoundException(String filePath) {
        super("Execution of the program has stopped because an instance of the order manager failed to be created. Specifically, the file at '" + filePath + "' could not be found. Double check the relative file path and re-run.");
    }
}
