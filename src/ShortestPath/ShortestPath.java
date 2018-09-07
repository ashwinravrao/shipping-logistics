/*
 * Developed by Ashwin Rao in June 2017.
 * Last Modified 9/7/18 12:43 PM.
 * Copyright (c) 2018. All rights reserved.
 */

package ShortestPath;

import Exceptions.DriveTimeException;
import Exceptions.MaxSpeedException;
import Exceptions.IllegalStartEndException;
import Facilities.FacilityInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;


public class ShortestPath {

    private HashMap<String, Double> mapLowMiles = new HashMap<>();
    private HashMap<String, String> mapLowDays = new HashMap<>();
    private HashMap<String, String> pairs = new HashMap<>();
    private ArrayList<String> mapNames = new ArrayList<>();
    private ArrayList<String> bestPath = new ArrayList<>();
    private ArrayList<String> lowPath = new ArrayList<>();
    private ArrayList<String> newPath;
    private double lowestDistanceMiles;
    private double avgMPH;
    private double drivingHoursPerDay;
    private String start;
    private String end;


    public ShortestPath(String start, String end, double avgMPH, double drivingHoursPerDay, ArrayList<FacilityInterface> facilities) {
        try {
            if (!facilities.isEmpty()) {
                setLocations(start, end, facilities);
                setSpeedAndDrivingHours(avgMPH, drivingHoursPerDay);
                mapLinksFromWarehouseObjs(facilities);
                findBestPath(this.start, this.end);
            }
        } catch (MaxSpeedException | DriveTimeException | IllegalStartEndException e) {
            System.err.println(e.getMessage());
        }
    }

    private void setLocations(String start, String end, ArrayList<FacilityInterface> facilities) throws IllegalStartEndException {
        setMapNames(facilities);
        boolean isStartSet = false;
        boolean isEndSet = false;
        for (String s : mapNames) {
            if (s.contains(start)) {
                this.start = start;
                isStartSet = true;
            }
            if (s.contains(end)) {
                this.end = end;
                isEndSet = true;
            }
        }
        if (!isStartSet || !isEndSet) {
            throw new IllegalStartEndException();
        }
    }

    private void setSpeedAndDrivingHours(double avgMPH, double drivingHoursPerDay) throws MaxSpeedException, DriveTimeException {
        if (avgMPH <= 85.0) {
            this.avgMPH = avgMPH;
        } else throw new MaxSpeedException();

        if (drivingHoursPerDay <= 14.0) {
            this.drivingHoursPerDay = drivingHoursPerDay;
        } else throw new DriveTimeException();
    }

    public HashMap<String, String> getMapLowDays() {
        return mapLowDays;
    }

    public void printShortestPath() {
        for (Object o : mapLowDays.entrySet()) {
            Map.Entry entry = (Map.Entry) o;
            String key = entry.getKey().toString().replace("[", "").replace("]", "").trim();
            String[] valuePre = entry.getValue().toString().split(" : ");
            String valueDaysStr = valuePre[1];
            String valueMilesStr = valuePre[0];
            Double valueDays = Double.parseDouble(valueDaysStr);
            Double valueMiles = Double.parseDouble(valueMilesStr);
            System.out.printf("\nSHORTEST PATH\n\nOrigin:               %s\nDestination:          %s\n\nShortest Path:        %s\nDriving Distance:     %.2f days (%.1f miles at %.1f mph, %.1f hours/day)", start, end, key, valueDays, valueMiles, avgMPH, drivingHoursPerDay);
            System.out.println("\n\n------------------------------------------------------------------------------------------------------------------------");
        }
    }

    private HashMap<String, String> findBestPath(String start, String end) {

        ArrayList<String> pathList = new ArrayList<>();
        pathList.add(start);
        ArrayList<String> found = findPaths(start, end, pathList, pairs);
        mapLowMiles.put(found.toString(), lowestDistanceMiles);
        String distanceValue = lowestDistanceMiles + " : " + (lowestDistanceMiles / (drivingHoursPerDay * avgMPH));
        mapLowDays.put(found.toString(), distanceValue);
        return mapLowDays;
    }

    private void mapLinksFromWarehouseObjs(ArrayList<FacilityInterface> facilities) {
        for (FacilityInterface w : facilities) {
            for (Object o : w.getLinkMap().entrySet()) {
                Map.Entry entry = (Map.Entry) o;
                pairs.put(entry.getKey().toString(), entry.getValue().toString());
            }
        }
    }

    private void setMapNames(ArrayList<FacilityInterface> facilities) {
        for (FacilityInterface w : facilities) {
            for (Object o : w.getLinkMap().entrySet()) {
                Map.Entry entry = (Map.Entry) o;
                mapNames.add(entry.getKey().toString());
            }
        }
    }

    private ArrayList<String> findPaths(String start, String end, ArrayList<String> pathList, HashMap<String, String> pairs) {

        if (!start.equals(end)) {
            HashSet<String> fromHere = new HashSet<>();

            for (Map.Entry<String, String> entry : pairs.entrySet()) {
                String key = entry.getKey();
                if (key.contains(start)) {
                    String[] keyArray = key.split(" : ");
                    String src = keyArray[0];
                    if (src.equals(start)) {
                        fromHere.add(key);
                    }
                }
            }
            String two;
            for (String s : fromHere) {
                String[] sSplit = s.split(" : ");
                two = sSplit[1];
                if (!pathList.contains(two)) {
                    newPath = new ArrayList<>(pathList);
                    newPath.add(two);
                    if (two.equals(end)) {
                        bestPath = lowPathFinder(newPath, pairs);
                    } else {
                        findPaths(two, end, newPath, pairs);
                    }
                }
            }
        }
        return bestPath;
    }

    private ArrayList<String> lowPathFinder(ArrayList<String> newPath, HashMap<String, String> pairs) {
        if (lowPath.isEmpty()) {
            lowPath = new ArrayList<>(newPath);
            for (int i = 0; i < lowPath.size() - 1; i++) {
                String lookup = lowPath.get(i) + " : " + lowPath.get(i + 1);
                String dist = pairs.get(lookup);
                lowestDistanceMiles += Integer.parseInt(dist);
            }
        } else {
            int tally = 0;
            for (int i = 0; i < newPath.size() - 1; i++) {
                String lookup = newPath.get(i) + " : " + newPath.get(i + 1);
                String dist = pairs.get(lookup);
                tally += Integer.parseInt(dist);
            }
            if (tally < lowestDistanceMiles) {
                lowestDistanceMiles = tally;
                lowPath = new ArrayList<>(newPath);
            }
        }
        return lowPath;
    }
}
