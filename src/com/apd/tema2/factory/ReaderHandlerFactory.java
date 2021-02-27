package com.apd.tema2.factory;

import com.apd.tema2.Main;
import com.apd.tema2.entities.Car;
import com.apd.tema2.entities.Intersection;
import com.apd.tema2.entities.Pedestrians;
import com.apd.tema2.entities.ReaderHandler;
import com.apd.tema2.intersections.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

/**
 * Returneaza sub forma unor clase anonime implementari pentru metoda de citire din fisier.
 */
public class ReaderHandlerFactory {

    public static ReaderHandler getHandler(String handlerType) {
        // simple semaphore intersection
        // max random N cars roundabout (s time to exit each of them)
        // roundabout with exactly one car from each lane simultaneously
        // roundabout with exactly X cars from each lane simultaneously
        // roundabout with at most X cars from each lane simultaneously
        // entering a road without any priority
        // crosswalk activated on at least a number of people (s time to finish all of them)
        // road in maintenance - 1 lane 2 ways, X cars at a time
        // road in maintenance - N lanes 2 ways, X cars at a time
        // railroad blockage for T seconds for all the cars
        // unmarked intersection
        // cars racing
        return switch (handlerType) {
            case "simple_semaphore" -> new ReaderHandler() {
                @Override
                public void handle(final String handlerType, final BufferedReader br) {
                    Main.intersection = IntersectionFactory.getIntersection("simple_semaphore_intersection");
                }
            };
            case "simple_n_roundabout" -> new ReaderHandler() {
                @Override
                public void handle(final String handlerType, final BufferedReader br) throws IOException {
                    String[] line = br.readLine().split(" ");
                    SimpleRoundabout intersection = (SimpleRoundabout) IntersectionFactory.getIntersection("simple_n_roundabout_intersection");

                    // semaforul va fi initializat cu numarul maxim de masini ce pot intra in intersectie la un anumit moment
                    intersection.setSem(new Semaphore(Integer.parseInt(line[0])));
                    intersection.setTime(Integer.parseInt(line[1]));

                    Main.intersection = intersection;
                }
            };
            case "simple_strict_1_car_roundabout" -> new ReaderHandler() {
                @Override
                public void handle(final String handlerType, final BufferedReader br) throws IOException {
                    String[] line = br.readLine().split(" ");
                    StrictRoundabout intersection = (StrictRoundabout) IntersectionFactory.getIntersection("simple_strict_1_car_roundabout_intersection");

                    intersection.setLaneNo(Integer.parseInt(line[0]));
                    intersection.setTime(Integer.parseInt(line[1]));

                    // fiecare banda va avea propriul semafor, initializat cu 1
                    ArrayList<Semaphore> sem = new ArrayList<>(Integer.parseInt(line[0]));
                    for (int i = 0; i < Integer.parseInt(line[0]); i++) {
                        sem.add(new Semaphore(1));
                    }
                    intersection.setSem(sem);

                    Main.intersection = intersection;
                }
            };
            case "simple_strict_x_car_roundabout" -> new ReaderHandler() {
                @Override
                public void handle(final String handlerType, final BufferedReader br) throws IOException {
                    String[] line = br.readLine().split(" ");
                    StrictRoundabout intersection = (StrictRoundabout) IntersectionFactory.getIntersection("simple_strict_x_car_roundabout_intersection");

                    intersection.setLaneNo(Integer.parseInt(line[0]));
                    intersection.setTime(Integer.parseInt(line[1]));

                    // fiecare banda va avea propriul semafor, initializat cu numarul maxim de maxini permise
                    ArrayList<Semaphore> sem = new ArrayList<>(Integer.parseInt(line[0]));
                    for (int i = 0; i < Integer.parseInt(line[0]); i++) {
                        sem.add(new Semaphore(Integer.parseInt(line[2])));
                    }
                    intersection.setSem(sem);

                    // folosita pentru sincronizarea masinilor
                    intersection.barrier = new CyclicBarrier(Integer.parseInt(line[0]) * Integer.parseInt(line[2]));

                    Main.intersection = intersection;
                }
            };
            case "simple_max_x_car_roundabout" -> new ReaderHandler() {
                @Override
                public void handle(final String handlerType, final BufferedReader br) throws IOException {
                    String[] line = br.readLine().split(" ");
                    StrictRoundabout intersection = (StrictRoundabout) IntersectionFactory.getIntersection("simple_max_x_car_roundabout_intersection");

                    intersection.setLaneNo(Integer.parseInt(line[0]));
                    intersection.setTime(Integer.parseInt(line[1]));

                    // fiecare banda va avea propriul semafor, initializat cu numarul maxim de masini permise
                    ArrayList<Semaphore> sem = new ArrayList<>(Integer.parseInt(line[0]));
                    for (int i = 0; i < Integer.parseInt(line[0]); i++) {
                        sem.add(new Semaphore(Integer.parseInt(line[2])));
                    }
                    intersection.setSem(sem);

                    Main.intersection = intersection;
                }
            };
            case "priority_intersection" -> new ReaderHandler() {
                @Override
                public void handle(final String handlerType, final BufferedReader br) throws IOException {
                    String[] line = br.readLine().split(" ");
                    PriorityIntersection intersection = (PriorityIntersection) IntersectionFactory.getIntersection("priority_intersection");

                    intersection.setCarsHighPriorityNo(Integer.parseInt(line[0]));
                    intersection.setCarsLowPriorityNo(Integer.parseInt(line[1]));
                    // masinile cu prioritate mare au nevoie de 2 secunde pentru a trece prin intersectie
                    intersection.setTime(2000);
                    intersection.setCarsHighPriorityInIntersection(0);

                    // semafoare utilizate pentru a respecta regula: maxim o masina fara prioritate atunci
                    // cand in intersectie nu se afla masini cu prioritate
                    Semaphore sem = new Semaphore(1);
                    intersection.setSemLowPriority(sem);
                    sem = new Semaphore(intersection.getCarsHighPriorityNo() + 1);
                    intersection.setSemHighPriority(sem);

                    Main.intersection = intersection;
                }
            };
            case "crosswalk" -> new ReaderHandler() {
                @Override
                public void handle(final String handlerType, final BufferedReader br) throws IOException {
                    String[] line = br.readLine().split(" ");

                    // timpul de executie si numarul maxim de pietonii
                    Main.pedestrians = new Pedestrians(Integer.parseInt(line[0]), Integer.parseInt(line[1]));
                    CrossWalkIntersection intersection = (CrossWalkIntersection) IntersectionFactory.getIntersection("crosswalk_intersection");

                    intersection.setQueue(Main.carsNo);
                    intersection.setIDAndLastColor(Main.carsNo);
                    intersection.setPedestriansNoMax(Integer.parseInt(line[1]));

                    Main.intersection = intersection;

                }
            };
            case "simple_maintenance" -> new ReaderHandler() {
                @Override
                public void handle(final String handlerType, final BufferedReader br) throws IOException {
                    String[] line = br.readLine().split(" ");
                    SimpleMaintenance intersection = (SimpleMaintenance) IntersectionFactory.getIntersection("simple_maintenance_intersection");

                    // numarul de masini ce trec dintr-un sens
                    intersection.setNoCarsPassingAtOnce(Integer.parseInt(line[0]));
                    intersection.setCarsLane0(0);
                    intersection.setCarsLane1(0);

                    CyclicBarrier barrier = new CyclicBarrier(Integer.parseInt(line[0]));
                    intersection.setBarrier(barrier);

                    // utilizate pentru trecerea alternativa
                    Semaphore sem = new Semaphore(Integer.parseInt(line[0]));
                    intersection.setSemLane0(sem);
                    sem = new Semaphore(0);
                    intersection.setSemLane1(sem);

                    Main.intersection = intersection;
                }
            };
            case "complex_maintenance" -> new ReaderHandler() {
                @Override
                public void handle(final String handlerType, final BufferedReader br) throws IOException {
                    String[] line = br.readLine().split(" ");
                    ComplexMaintenance intersection = (ComplexMaintenance) IntersectionFactory.getIntersection("complex_maintenance_intersection");

                    intersection.setFreeLanes(Integer.parseInt(line[0]));
                    intersection.setOldLanes(Integer.parseInt(line[1]));
                    intersection.setMaxCars(Integer.parseInt(line[2]));

                    ConcurrentHashMap<Integer, BlockingQueue<Car>> cars = new ConcurrentHashMap<>();
                    intersection.setCars(cars);

                    CyclicBarrier barrier = new CyclicBarrier(Main.carsNo);
                    intersection.setBarrier(barrier);

                    Semaphore sem = new Semaphore(intersection.getFreeLanes() * intersection.getMaxCars());
                    intersection.setSemRound(sem);
                    intersection.setIntervals(Integer.parseInt(line[1]), Integer.parseInt(line[0]));

                    // semafoare pt lane-uri noi
                    ArrayList<Semaphore> sems = new ArrayList<>();
                    for (int i = 0; i < intersection.getFreeLanes(); i++) {
                        sems.add(new Semaphore(intersection.getMaxCars()));
                    }
                    intersection.setSems(sems);

                    Main.intersection = intersection;

                }
            };
            case "railroad" -> new ReaderHandler() {
                @Override
                public void handle(final String handlerType, final BufferedReader br) throws IOException {
                    RailRoad intersection = (RailRoad) IntersectionFactory.getIntersection("railroad_intersection");

                    CyclicBarrier barrier = new CyclicBarrier(Main.carsNo);
                    intersection.setBarrierCars(barrier);

                    ConcurrentHashMap<Integer, BlockingQueue<Car>> cars = new ConcurrentHashMap<Integer, BlockingQueue<Car>>();
                    intersection.setCars(cars);

                    Main.intersection = intersection;
                }
            };
            default -> null;
        };
    }
}
