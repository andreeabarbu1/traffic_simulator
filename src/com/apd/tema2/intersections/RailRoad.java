package com.apd.tema2.intersections;

import com.apd.tema2.entities.Car;
import com.apd.tema2.entities.Intersection;

import java.util.HashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CyclicBarrier;

/**
 * Clasa RailRoad va fi folosita pentru railroad_intersection
 */
public class RailRoad implements Intersection {
    private CyclicBarrier barrierCars;
    private ConcurrentHashMap<Integer, BlockingQueue<Car>> cars; // pentru a retine ordinea masinilor

    public RailRoad() {
    }

    public CyclicBarrier getBarrierCars() {
        return barrierCars;
    }

    public void setBarrierCars(CyclicBarrier barrierCars) {
        this.barrierCars = barrierCars;
    }

    public synchronized ConcurrentHashMap<Integer, BlockingQueue<Car>> getCars() {
        return cars;
    }

    public void setCars(ConcurrentHashMap<Integer, BlockingQueue<Car>> cars) {
        this.cars = cars;
    }
}
