package com.apd.tema2.intersections;

import com.apd.tema2.Main;
import com.apd.tema2.entities.Car;
import com.apd.tema2.entities.Intersection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.*;

public class ComplexMaintenance implements Intersection {
    private int freeLanes;
    private int oldLanes;
    private int maxCars;
    private CyclicBarrier barrier;
    private CyclicBarrier barrierFreeLanes;
    // lane - {masini} initial
    private ConcurrentHashMap<Integer, BlockingQueue<Car>> cars;
    private ArrayList<Pair> intervals;
    private ArrayList<Semaphore> sems;
    private Semaphore semRound;
    private ConcurrentHashMap<Integer, ArrayList<Integer>> intervalsQueue;

    class Pair {
        int start;
        int end;

        public Pair(int start, int end) {
            this.start = start;
            this.end = end;
        }

        public int getStart() {
            return start;
        }

        public void setStart(int start) {
            this.start = start;
        }

        public int getEnd() {
            return end;
        }

        public void setEnd(int end) {
            this.end = end;
        }
    }

    public int getYourLane (int lane, ArrayList<Pair> intervals) {
        for (int i = 0; i < intervals.size(); i++) {
            if (lane >= intervals.get(i).getStart() && lane < intervals.get(i).getEnd()) {
                return i;
            }
        }
        return -1;
    }

    public Semaphore getSemRound() {
        return semRound;
    }

    public void setSemRound(Semaphore semRound) {
        this.semRound = semRound;
    }

    public CyclicBarrier getBarrierFreeLanes() {
        return barrierFreeLanes;
    }

    public void setBarrierFreeLanes(CyclicBarrier barrierFreeLanes) {
        this.barrierFreeLanes = barrierFreeLanes;
    }

    public void setIntervals (int oldLanes, int freeLanes) {
        intervals = new ArrayList<>();
        intervalsQueue = new ConcurrentHashMap<>();

        for (int i = 0; i < freeLanes; i++) {
            int start = (int)(i * (double)oldLanes / freeLanes);
            int end = (int)Math.min((i + 1) * (double)oldLanes / freeLanes, oldLanes);
            Pair current = new Pair(start, end);
            intervals.add(current);
            intervalsQueue.putIfAbsent(i, new ArrayList<>(oldLanes));
            for (int j = start; j < end; j++) {
                intervalsQueue.get(i).add(j);
            }
        }
    }

    public ConcurrentHashMap<Integer, ArrayList<Integer>> getIntervalsQueue() {
        return intervalsQueue;
    }

    public ArrayList<Pair> getIntervals() {
        return intervals;
    }


    public ArrayList<Semaphore> getSems() {
        return sems;
    }

    public void setSems(ArrayList<Semaphore> sems) {
        this.sems = sems;
    }

    public ComplexMaintenance() {
    }

    public CyclicBarrier getBarrier() {
        return barrier;
    }

    public void setBarrier(CyclicBarrier barrier) {
        this.barrier = barrier;
    }

    public ConcurrentHashMap<Integer, BlockingQueue<Car>> getCars() {
        return cars;
    }

    public void setCars(ConcurrentHashMap<Integer, BlockingQueue<Car>> cars) {
        this.cars = cars;
    }


    public int getFreeLanes() {
        return freeLanes;
    }

    public void setFreeLanes(int freeLanes) {
        this.freeLanes = freeLanes;
    }

    public int getOldLanes() {
        return oldLanes;
    }

    public void setOldLanes(int oldLanes) {
        this.oldLanes = oldLanes;
    }

    public int getMaxCars() {
        return maxCars;
    }

    public void setMaxCars(int maxCars) {
        this.maxCars = maxCars;
    }


}
