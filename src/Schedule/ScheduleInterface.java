/*
 * Developed by Ashwin Rao in June 2017.
 * Last Modified 9/7/18 12:43 PM.
 * Copyright (c) 2018. All rights reserved.
 */

package Schedule;


import Exceptions.BrokenLoaderFieldsException;
import Exceptions.UpdateFailedException;

import java.util.ArrayList;

public interface ScheduleInterface {


    ArrayList<ArrayList<Integer>> getSchedule();
    void setSchedule(int pRate) throws BrokenLoaderFieldsException;
    void printSchedule();
}
