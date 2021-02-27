package com.apd.tema2.intersections;

import com.apd.tema2.entities.Car;
import com.apd.tema2.entities.Intersection;

import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

/**
 * Clasa CrossWalkIntersection va fi folosita pentru crosswalk
 */
public class CrossWalkIntersection implements Intersection {
    private BlockingQueue<Car> queue; // pentru a retine masinile ce asteapta la semafor
    private HashMap<Integer, String> IDAndLastColor; // pentru a retine istoricul mesajelor
    private int pedestriansNoMax;

    public CrossWalkIntersection() {
    }

    public int getPedestriansNoMax() {
        return pedestriansNoMax;
    }

    public void setPedestriansNoMax(int pedestriansNoMax) {
        this.pedestriansNoMax = pedestriansNoMax;
    }

    public BlockingQueue getQueue() {
        return queue;
    }

    public HashMap<Integer, String> getIDAndLastColor() {
        return IDAndLastColor;
    }

    public void setIDAndLastColor(int carsNo) {
        IDAndLastColor = new HashMap<>();
        for (int i = 0; i < carsNo; i++) {
            IDAndLastColor.put(i, "null");
        }
    }

    public void setQueue(int carsNo) {
        this.queue = new ArrayBlockingQueue<Car>(carsNo);
    }

}
