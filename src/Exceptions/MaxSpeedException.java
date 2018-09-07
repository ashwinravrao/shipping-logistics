/*
 * Developed by Ashwin Rao in June 2017.
 * Last Modified 9/7/18 12:43 PM.
 * Copyright (c) 2018. All rights reserved.
 */

package Exceptions;


public class MaxSpeedException extends Exception {

    public MaxSpeedException() {
        super("You cannot exceed an average speed of 85 miles per hour, according to US DOT rules. Additionally, your average speed must be non-zero and positive. A common speed is 50.0 mph. Check your average speed argument and re-run.");
    }

}
