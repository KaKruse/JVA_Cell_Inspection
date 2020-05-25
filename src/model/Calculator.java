package model;

import java.text.SimpleDateFormat;
import java.util.*;

public class Calculator {

    /**
     * List of cell numbers
     */
    private final List<String> cells;
    /**
     * Number of cells to cells to inspect
     * Calculated by 10 week days with 4 cells to inspect each day
     */
    private static final int capacity = 40;

    /**
     * Constructor
     * Initializes the list of cells
     */
    public Calculator() {
        //ToDo read from a file;
        this.cells = new ArrayList<>(
                Arrays.asList("5", "6", "7", "8", "9", "10", "11", "13", "14", "15", "16", "17", "18", "19", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36")
        );
    }

    /**
     *
     * @param date date of calculation
     * @return a two week inspection plan
     */
    public List<Pair<String, List<String>>> calc_inspections(Date date) {
        // Output array
        List<Pair<String, List<String>>> out = new ArrayList<>();

        //Create a list of 40 randoms. Each entry is between 0 and the size of the cell list minus 1
        Random ran = new Random();
        List<Integer> randoms = new ArrayList<>();
        for(int i = 0; i < capacity; i++) {
            randoms.add(ran.nextInt(this.cells.size()));
        }

        //Make sure no cell occurs more than capacity/4 times (occurrence only one time a day), or capacity minus cell list size. Depends on which number is smaller
        boolean condition = true;
        while(condition) {
            for(int i = 0; i < this.cells.size(); i++) {
                if(capacity / 4 < capacity - this.cells.size()) {
                    if (Collections.frequency(randoms, i) > (capacity / 4)) {
                        int j = randoms.lastIndexOf(i);
                        randoms.remove(j);
                        randoms.add(j, ran.nextInt(this.cells.size()));
                        break;
                    }
                } else {
                    if (Collections.frequency(randoms, i) > capacity - this.cells.size()) {
                        int j = randoms.lastIndexOf(i);
                        randoms.remove(j);
                        randoms.add(j, ran.nextInt(this.cells.size()));
                        break;
                    }
                }
                if(i == this.cells.size() - 1) {
                    condition = false;
                }
            }
        }

        //Get all positions occurring more than one time
        Map<Integer, Integer> occ = new HashMap<>();
        for(int i = 0; i < this.cells.size(); i++) {
            if(Collections.frequency(randoms, i) > 1) {
                for(int j = 0; j < randoms.size(); j++) {
                    if(randoms.get(j) == i) {
                        occ.put(j, i);
                    }
                }
            }
        }

        //Make sure every cell is at least one time in the random number list.
        for(int i = 0; i < this.cells.size(); i++) {
            if(!randoms.contains(i)) {
                int pos = ran.nextInt(occ.keySet().size());
                randoms.remove(((Integer) occ.keySet().toArray()[pos]).intValue());
                randoms.add((Integer) occ.keySet().toArray()[pos], i);

                occ.remove(occ.keySet().toArray()[pos]);
                for(int j = 0; j < occ.values().size(); j++) {
                    if(Collections.frequency(occ.values(), occ.values().toArray()[j]) <= 1) {
                        occ.remove(occ.keySet().toArray()[j]);
                        j--;
                    }
                }
            }
        }
        //Make day slices
        List<String> slice = new ArrayList<>();
        List<List<String>> slices = new ArrayList<>();
        for(int i = 0; i < randoms.size(); i++) {

            slice.add(this.cells.get(randoms.get(i)));
            if (i % 4 == 3) {
                slices.add(slice);
                slice = new ArrayList<>();
            }
        }


        //Make sure a slice does not contain a cell twice
        for(int i = 0; i < slices.size(); i++) {
            for(int j = 0; j < slices.get(i).size() - 1; j++) {
                if(Collections.frequency(slices.get(i), slices.get(i).get(j)) > 1) {
                    for(int k = 0; k < Math.max(i, slices.size() - 1 - i); k++) {
                        if(i - k > 0 && !slices.get(i - k).contains(slices.get(i).get(j))) {
                            String tmp = slices.get(i).remove(j);
                            slices.get(i).add(j, slices.get(i - k).remove(j));
                            slices.get(i - k).add(j, tmp);
                        } else if(i + k < slices.size() && !slices.get(i + k).contains(slices.get(i).get(j))) {
                            String tmp = slices.get(i).remove(j);
                            slices.get(i).add(j, slices.get(i + k).remove(j));
                            slices.get(i + k).add(j, tmp);
                        }
                    }
                }
            }
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        //Make sure start date is a monday
        switch(cal.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.TUESDAY:
                cal.add(Calendar.DATE, -1);
                break;
            case Calendar.WEDNESDAY:
                cal.add(Calendar.DATE, -2);
                break;
            case Calendar.THURSDAY:
                cal.add(Calendar.DATE, -3);
                break;
            case Calendar.FRIDAY:
                cal.add(Calendar.DATE, -4);
                break;
            case Calendar.SATURDAY:
                cal.add(Calendar.DATE, 2);
                break;
            case Calendar.SUNDAY:
                cal.add(Calendar.DATE, 1);
                break;
            default:
                break;
        }

        //Build output
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        for(int i = 0; i < slices.size(); i++) {
            if(cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                out.add(new Pair<>(formatter.format(cal.getTime()), new ArrayList<>(Arrays.asList("-", "-", "-", "-"))));
                i--;
            } else {
                out.add(new Pair<>(formatter.format(cal.getTime()), slices.get(i)));
            }
            cal.add(Calendar.DATE, 1);
        }
        out.add(new Pair<>(formatter.format(cal.getTime()), new ArrayList<>(Arrays.asList("-", "-", "-", "-"))));
        cal.add(Calendar.DATE, 1);
        out.add(new Pair<>(formatter.format(cal.getTime()), new ArrayList<>(Arrays.asList("-", "-", "-", "-"))));
        return out;
    }

}
