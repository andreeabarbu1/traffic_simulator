package com.apd.tema2.intersections;

import com.apd.tema2.entities.Intersection;

import java.util.concurrent.Semaphore;

/**
 * Clasa SimpleRoundabout va fi folosita pentru o intersectie simple_n_roundabout
 */
public class SimpleRoundabout implements Intersection {
    private Semaphore sem; // folosit pentru sincronizarea masinilor
    private int time;

    public SimpleRoundabout() {
    }

    public Semaphore getSem() {
        return sem;
    }

    public int getTime() {
        return time;
    }

    public void setSem(Semaphore sem) {
        this.sem = sem;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
