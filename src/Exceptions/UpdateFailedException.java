/*
 * Developed by Ashwin Rao in June 2017.
 * Last Modified 9/7/18 12:43 PM.
 * Copyright (c) 2018. All rights reserved.
 */

package Exceptions;

/**
 * Created by ashwinrao on 5/23/17.
 */
public class UpdateFailedException extends Exception {

    public UpdateFailedException(String reasonFailed) {
        super("The update could not be completed because " + reasonFailed);
    }
}
