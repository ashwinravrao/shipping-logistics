/*
 * Developed by Ashwin Rao in June 2017.
 * Last Modified 9/7/18 12:43 PM.
 * Copyright (c) 2018. All rights reserved.
 */

package Exceptions;

/**
 * Created by ashwinrao on 5/31/17.
 */
public class OrderSolutionException extends Exception {

    public OrderSolutionException() {
        super("There was a problem when trying to process the Order Solutions");
    }
}
