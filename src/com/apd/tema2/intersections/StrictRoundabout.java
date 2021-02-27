package com.apd.tema2.intersections;

import com.apd.tema2.entities.Intersection;

import java.util.ArrayList;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

/**
 * Clasa StrictRoundabout va fi folosita pentru urmatoarele intersectii strict_1_roundabout, strict_n_roundabout si
 * simple_max_x_car_roundabout
 */
public class StrictRoundabout  implements Intersection {
    private int laneNo;
    private int time;
    private ArrayList<Semaphore> sem = new ArrayList<>(laneNo); // folosit pentru sincronizarea masinilor
    public static CyclicBarrier barrier;

    public StrictRoundabout() {
    }

    public void setLaneNo(int laneNo) {
        this.laneNo = laneNo;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getTime() {
        return time;
    }

    public ArrayList<Semaphore> getSem() {
        return sem;
    }

    public void setSem(ArrayList<Semaphore> sem) {
        this.sem = sem;
    }
}
