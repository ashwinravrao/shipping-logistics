/*
 * Developed by Ashwin Rao in June 2017.
 * Last Modified 9/7/18 12:43 PM.
 * Copyright (c) 2018. All rights reserved.
 */

package Exceptions;


public class DriveTimeException extends Exception {

    public DriveTimeException() {
        super("You cannot exceed an average of 14 hours of driving per day, according to US DOT rules. Additionally, your average driving hours must be non-zero and positive. A common average number of driving hours is 8.0. Check your driving hours argument and re-run.");
    }
}
