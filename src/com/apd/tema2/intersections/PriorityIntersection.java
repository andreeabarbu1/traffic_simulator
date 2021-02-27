package com.apd.tema2.intersections;

import com.apd.tema2.entities.Intersection;

import java.util.concurrent.Semaphore;

/**
 * Clasa PriorityIntersection va fi folosita pentru priority_intersection
 */
public class PriorityIntersection implements Intersection {
    private int carsHighPriorityNo;
    private int carsLowPriorityNo;
    private Semaphore semLowPriority;
    private Semaphore semHighPriority;
    private int timeHighPriority;
    private int carsHighPriorityInIntersection;


    public PriorityIntersection() {
    }

    public Semaphore getSemHighPriority() {
        return semHighPriority;
    }

    public void setSemHighPriority(Semaphore semHighPriority) {
        this.semHighPriority = semHighPriority;
    }

    public void setCarsHighPriorityInIntersection(int carsHighPriorityinIntersection) {
        this.carsHighPriorityInIntersection = carsHighPriorityinIntersection;
    }

    public void addCar() {
        this.carsHighPriorityInIntersection++;
    }

    public void subsCar() {
        this.carsHighPriorityInIntersection--;
    }

    public int getCarsHighPriorityNo() {
        return carsHighPriorityNo;
    }

    public void setCarsHighPriorityNo(int carsHighPriorityNo) {
        this.carsHighPriorityNo = carsHighPriorityNo;
    }

    public Semaphore getSemLowPriority() {
        return semLowPriority;
    }

    public void setSemLowPriority(Semaphore semLowPriority) {
        this.semLowPriority = semLowPriority;
    }

    public int getTime() {
        return timeHighPriority;
    }

    public void setTime(int timeHighPriority) {
        this.timeHighPriority = timeHighPriority;
    }

    public void setCarsLowPriorityNo(int carsLowPriorityNo) {
        this.carsLowPriorityNo = carsLowPriorityNo;
    }
}
