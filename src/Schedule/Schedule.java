/*
 * Developed by Ashwin Rao in June 2017.
 * Last Modified 9/7/18 12:43 PM.
 * Copyright (c) 2018. All rights reserved.
 */

package Schedule;


import Exceptions.BrokenLoaderFieldsException;
import Exceptions.UpdateFailedException;
import java.util.ArrayList;


public class Schedule implements ScheduleInterface {

    private ArrayList<ArrayList<Integer>> schedule = new ArrayList<>();
    private ArrayList<Integer> days = new ArrayList<>();
    private ArrayList<Integer> available = new ArrayList<>();

    public Schedule(int processingRate) throws BrokenLoaderFieldsException {
        setSchedule(processingRate);
    }

    @Override
    public void setSchedule(int processingRate) throws BrokenLoaderFieldsException {
        if (processingRate > 0) {
            for (int i=1; i <= 50; i++) {   // counting days
                days.add(i);
                available.add(processingRate);
            }
            schedule.add(days);
            schedule.add(available);
        } else throw new BrokenLoaderFieldsException("int processingRate");
    }

    public void updateSchedule(String facilityName, ArrayList<Integer> modifiedSchedule) throws UpdateFailedException {
        if (!modifiedSchedule.isEmpty()) {
            ArrayList<Integer> newSchedule = new ArrayList<>(modifiedSchedule);
            schedule.remove(1);
            schedule.add(newSchedule);
        } else throw new UpdateFailedException("The schedule for " + facilityName + " failed to updated on order processing");
    }

    @Override
    public ArrayList<ArrayList<Integer>> getSchedule() {
        return schedule;
    }

    @Override
    public void printSchedule() {
        System.out.println("\n\nSchedule:");
        System.out.print("Day:           ");
        for (Integer i : schedule.get(0)) {
            System.out.printf("%5d", i);
        }
        System.out.println();
        System.out.print("Available:     ");
        for (Integer i : schedule.get(1)) {
            System.out.printf("%5d", i);
        }
        System.out.println("\n");
        System.out.println("----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n");
    }
}
