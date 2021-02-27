package com.apd.tema2.intersections;

import com.apd.tema2.entities.Intersection;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

/**
 * Clasa SimpleMaintenance va fi folosita pentru simple_maintenance_intersection
 */
public class SimpleMaintenance implements Intersection {
    private int noCarsPassingAtOnce;
    private Semaphore semLane0; // pentru sincronizarea masinilor de pe prima banda
    private Semaphore semLane1; // pentru sincronizarea masinilor de pe a doua banda
    private int carsLane0;
    private int carsLane1;
    private CyclicBarrier barrier;

    public SimpleMaintenance() {
    }

    public CyclicBarrier getBarrier() {
        return barrier;
    }

    public void setBarrier(CyclicBarrier barrier) {
        this.barrier = barrier;
    }

    public int getCarsLane0() {
        return carsLane0;
    }

    public void setCarsLane0(int carsLane0) {
        this.carsLane0 = carsLane0;
    }

    public int getCarsLane1() {
        return carsLane1;
    }

    public void addCarsLane0() {
        carsLane0++;
    }

    public void addCarsLane1() {
        carsLane1++;
    }

    public void setCarsLane1(int carsLane1) {
        this.carsLane1 = carsLane1;
    }

    public int getNoCarsPassingAtOnce() {
        return noCarsPassingAtOnce;
    }

    public void setNoCarsPassingAtOnce(int noCarsPassingAtOnce) {
        this.noCarsPassingAtOnce = noCarsPassingAtOnce;
    }

    public Semaphore getSemLane0() {
        return semLane0;
    }

    public void setSemLane0(Semaphore semLane0) {
        this.semLane0 = semLane0;
    }

    public Semaphore getSemLane1() {
        return semLane1;
    }

    public void setSemLane1(Semaphore semLane1) {
        this.semLane1 = semLane1;
    }
}
