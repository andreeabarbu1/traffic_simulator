package com.apd.tema2.factory;

import com.apd.tema2.Main;
import com.apd.tema2.entities.*;
import com.apd.tema2.intersections.*;
import com.apd.tema2.utils.Constants;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.Semaphore;

import static java.lang.Thread.sleep;

/**
 * Clasa Factory ce returneaza implementari ale InterfaceHandler sub forma unor
 * clase anonime.
 */
public class IntersectionHandlerFactory {

    public static IntersectionHandler getHandler(String handlerType) {
        // simple semaphore intersection
        // max random N cars roundabout (s time to exit each of them)
        // roundabout with exactly one car from each lane simultaneously
        // roundabout with exactly X cars from each lane simultaneously
        // roundabout with at most X cars from each lane simultaneously
        // entering a road without any priority
        // crosswalk activated on at least a number of people (s time to finish all of
        // them)
        // road in maintenance - 2 ways 1 lane each, X cars at a time
        // road in maintenance - 1 way, M out of N lanes are blocked, X cars at a time
        // railroad blockage for s seconds for all the cars
        // unmarked intersection
        // cars racing
        return switch (handlerType) {
            case "simple_semaphore" -> new IntersectionHandler() {
                @Override
                public void handle(Car car) {
                    System.out.println("Car " + car.getId() + " has reached the semaphore, now waiting...");
                    try {
                        sleep(car.getWaitingTime());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("Car " + car.getId() + " has waited enough, now driving...");
                }
            };
            case "simple_n_roundabout" -> new IntersectionHandler() {
                @Override
                public void handle(Car car) {
                    System.out.println("Car " + car.getId() + " has reached the roundabout, now waiting...");
                    int time = ((SimpleRoundabout) Main.intersection).getTime();

                    try {
                        // doar daca nu sunt maxim n masini in intersectie
                        ((SimpleRoundabout) Main.intersection).getSem().acquire();
                        System.out.println("Car " + car.getId() + " has entered the roundabout");

                        // asteapta cateva secunde pentru a iesi din intersectie
                        sleep(time);
                    } catch (InterruptedException exc) {
                        System.out.println(exc);
                    }

                    System.out.println("Car " + car.getId() + " has exited the roundabout after " + (time / 1000) + " seconds");
                    ((SimpleRoundabout) Main.intersection).getSem().release();
                }
            };
            case "simple_strict_1_car_roundabout" -> new IntersectionHandler() {
                @Override
                public void handle(Car car) {
                    System.out.println("Car " + car.getId() + " has reached the roundabout");
                    int lane = car.getStartDirection();
                    int time = ((StrictRoundabout) Main.intersection).getTime();

                    try {
                        // doar daca in intersectie nu se afla o alta masina de pe banda thread-ului curent
                        ((StrictRoundabout) Main.intersection).getSem().get(lane).acquire();
                        System.out.println("Car " + car.getId() + " has entered the roundabout from lane " + lane);

                        sleep(time);
                    } catch (InterruptedException exc) {
                        System.out.println(exc);
                    }

                    System.out.println("Car " + car.getId() + " has exited the roundabout after " + (time / 1000) + " seconds");
                    ((StrictRoundabout) Main.intersection).getSem().get(lane).release();
                }
            };
            case "simple_strict_x_car_roundabout" -> new IntersectionHandler() {
                @Override
                public void handle(Car car) {
                    System.out.println("Car " + car.getId() + " has reached the roundabout, now waiting...");
                    // asteapta ca toate masinile sa ajunga la intersectie
                    try {
                        Main.barrierCars.await();
                    } catch (BrokenBarrierException | InterruptedException e) {
                        e.printStackTrace();
                    }

                    int lane = car.getStartDirection();
                    int time = ((StrictRoundabout) Main.intersection).getTime();

                    try {
                        // doar daca nu a fost selectat numarul maxim de masini
                        ((StrictRoundabout) Main.intersection).getSem().get(lane).acquire();
                    } catch (InterruptedException exc) {
                        System.out.println(exc);
                    }
                    System.out.println("Car " + car.getId() + " was selected to enter the roundabout from lane " + lane);

                    // sincronizarea masinilor pentru intrarea in intersectie
                    try {
                        ((StrictRoundabout) Main.intersection).barrier.await();
                    } catch (BrokenBarrierException | InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("Car " + car.getId() + " has entered the roundabout from lane " + lane);

                    // asteapta intervalul de timp dat pentru traversarea intersectiei
                    try {
                        sleep(time);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("Car " + car.getId() + " has exited the roundabout after " + (time / 1000) + " seconds");

                    // sincronizarea masinilor pentru iesirea din intersectie
                    try {
                        ((StrictRoundabout) Main.intersection).barrier.await();
                    } catch (BrokenBarrierException | InterruptedException e) {
                        e.printStackTrace();
                    }
                    ((StrictRoundabout) Main.intersection).getSem().get(lane).release();
                }
            };
            case "simple_max_x_car_roundabout" -> new IntersectionHandler() {
                @Override
                public void handle(Car car) {
                    int lane = car.getStartDirection();
                    int time = ((StrictRoundabout) Main.intersection).getTime();
                    System.out.println("Car " + car.getId() + " has reached the roundabout from lane " + lane);

                    try {
                        sleep(car.getWaitingTime());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } // NU MODIFICATI

                    // Continuati de aici

                    try {
                        // doar daca nu se afla numarul maxim de masini de pe banda curenta in intersectie
                        ((StrictRoundabout) Main.intersection).getSem().get(lane).acquire();
                        System.out.println("Car " + car.getId() + " has entered the roundabout from lane " + lane);

                        sleep(time);
                    } catch (InterruptedException exc) {
                        System.out.println(exc);
                    }

                    System.out.println("Car " + car.getId() + " has exited the roundabout after " + (time / 1000) + " seconds");
                    ((StrictRoundabout) Main.intersection).getSem().get(lane).release();
                }
            };
            case "priority_intersection" -> new IntersectionHandler() {
                @Override
                public void handle(Car car) {
                    PriorityIntersection intersection = ((PriorityIntersection) Main.intersection);
                    try {
                        sleep(car.getWaitingTime());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } // NU MODIFICATI
                    // Continuati de aici

                    if (car.getPriority() == 1) {
                        System.out.println("Car " + car.getId() + " with low priority is trying to enter the intersection...");
                    }

                    // masina cu prioritate
                    if (car.getPriority() > 1) {
                        try {
                            intersection.getSemHighPriority().acquire();
                        } catch (InterruptedException exc) {
                            System.out.println(exc);
                        }

                        System.out.println("Car " + car.getId() + " with high priority has entered the intersection");
                        // stocheaza numarul de masini cu prioritate care se afla in intersectie

                        try {
                            // timpul de traversare a intersectiei
                            sleep(intersection.getTime());
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        // o masina cu prioritate a iesit din intersectie
                        System.out.println("Car " + car.getId() + " with high priority has exited the intersection");
                        intersection.getSemHighPriority().release();
                    }

                    // masina fara prioritate; va intra in intersectie daca nu sunt masini cu prioritate in ea
                    else if (car.getPriority() == 1) {
                        try {
                            // doar daca in intersectie nu se afla o masina cu prioritate
                            intersection.getSemLowPriority().acquire();
                            intersection.getSemHighPriority().acquire(intersection.getCarsHighPriorityNo() + 1);
                        } catch (InterruptedException exc) {
                            System.out.println(exc);
                        }

                        // masina iese din intersectie instant
                        System.out.println("Car " + car.getId() + " with low priority has entered the intersection");
                        intersection.getSemHighPriority().release(intersection.getCarsHighPriorityNo() + 1);
                        intersection.getSemLowPriority().release();
                    }
                }
            };
            case "crosswalk" -> new IntersectionHandler() {
                @Override
                public void handle(Car car) {
                    CrossWalkIntersection intersection = ((CrossWalkIntersection) Main.intersection);

                    // pana ce se termina timpul de executie
                    while (!Main.pedestrians.isFinished()) {
                        // daca trec pietonii
                        if (Main.pedestrians.getPedestriansNo() == intersection.getPedestriansNoMax()) {
                            if (!intersection.getIDAndLastColor().get(car.getId()).equals("red")) {
                                // adauga masina curenta in coada
                                intersection.getQueue().add(car);
                                System.out.println("Car " + car.getId() + " has now red light");
                                // retine ultimul mesaj asociat masinii
                                intersection.getIDAndLastColor().put(car.getId(), "red");
                            }

                            // daca nu trec pietoni
                        } else {
                            if (!intersection.getIDAndLastColor().get(car.getId()).equals("green")) {
                                // extrage masina din coada
                                intersection.getQueue().remove(car);
                                System.out.println("Car " + car.getId() + " has now green light");
                                // retine ultimul mesaj asociat masinii
                                intersection.getIDAndLastColor().put(car.getId(), "green");
                            }
                        }
                    }
                }
            };
            case "simple_maintenance" -> new IntersectionHandler() {
                @Override
                public void handle(Car car) {
                    SimpleMaintenance intersection = ((SimpleMaintenance) Main.intersection);
                    int lane = car.getStartDirection();
                    System.out.println("Car " + car.getId() + " from side number " + lane + " has reached the bottleneck");

                    // prima banda
                    if (lane == 0) {
                        try {
                            // acquire pe semaforul primei directii
                            intersection.getSemLane0().acquire();
                        } catch (InterruptedException exc) {
                            System.out.println(exc);
                        }

                        // numarul de masini ce trec intersectia de pe prima banda
                        intersection.addCarsLane0();
                        System.out.println("Car " + car.getId() + " from side number " + lane + " has passed the bottleneck");

                        // asteapta ca masinile de pe primul sens sa treaca de bottleneck
                        try {
                            intersection.getBarrier().await();
                        } catch (InterruptedException | BrokenBarrierException e) {
                            e.printStackTrace();
                        }

                        synchronized (intersection) {
                            // daca a trecut numarul specificat de masini
                            if (intersection.getCarsLane0() / intersection.getNoCarsPassingAtOnce() == 1 && intersection.getCarsLane0() != 0) {
                                // release pe semaforul celei de-a doua benzi cu X permise
                                intersection.getSemLane1().release(intersection.getNoCarsPassingAtOnce());
                                intersection.setCarsLane0(0);
                            }
                        }
                        // a doua banda
                    } else if (lane == 1) {
                        try {
                            //  acquire pe semaforul celei de-a doua benzi
                            intersection.getSemLane1().acquire();
                        } catch (InterruptedException exc) {
                            System.out.println(exc);
                        }

                        intersection.addCarsLane1();
                        System.out.println("Car " + car.getId() + " from side number " + lane + " has passed the bottleneck");

                        // asteapta ca masinile de pe al doilea sens sa treaca de bottleneck
                        try {
                            intersection.getBarrier().await();
                        } catch (InterruptedException | BrokenBarrierException e) {
                            e.printStackTrace();
                        }

                        synchronized (intersection) {
                            // daca a trecut numarul specificat de masini
                            if (intersection.getCarsLane1() / intersection.getNoCarsPassingAtOnce() == 1 && intersection.getCarsLane1() != 0) {
                                // release pe semaforul primei benzi cu X permise
                                intersection.getSemLane0().release(intersection.getNoCarsPassingAtOnce());
                                intersection.setCarsLane1(0);
                            }
                        }
                    }
                }
            };
            case "complex_maintenance" -> new IntersectionHandler() {
                @Override
                public void handle(Car car) {
                    ComplexMaintenance intersection = ((ComplexMaintenance) Main.intersection);
                    int lane = car.getStartDirection();

                }
            };
            case "railroad" -> new IntersectionHandler() {
                @Override
                public void handle(Car car) {
                    RailRoad intersection = ((RailRoad) Main.intersection);
                    int lane = car.getStartDirection();

                    synchronized (intersection) {
                        // adauga masinile in map in ordinea in care au venit
                        intersection.getCars().putIfAbsent(lane, new ArrayBlockingQueue<>(Main.carsNo));
                        intersection.getCars().get(lane).add(car);
                        System.out.println("Car " + car.getId() + " from side number " + lane + " has stopped by the railroad");
                    }

                    // asteapta ca toate masinile sa ajunga la bariera
                    try {
                        intersection.getBarrierCars().await();
                    } catch (BrokenBarrierException | InterruptedException e) {
                        e.printStackTrace();
                    }

                    if (car.getId() == 0) {
                        System.out.println("The train has passed, cars can now proceed");
                    }

                    // asteapta toate thread-urile
                    try {
                        intersection.getBarrierCars().await();
                    } catch (BrokenBarrierException | InterruptedException e) {
                        e.printStackTrace();
                    }

                    synchronized (intersection) {
                        // pana ce masina curenta este diferita de prima masina din coada lane-ului
                        while (car.getId() != intersection.getCars().get(lane).peek().getId()) {
                            try {
                                intersection.wait();
                            } catch (InterruptedException exc) {
                                exc.printStackTrace();
                            }
                        }

                        // daca este prima masina din coada lane-ului curent
                        if (car.getId() == intersection.getCars().get(lane).peek().getId()) {
                            // sterge masina din coada
                            intersection.getCars().get(lane).remove();
                            System.out.println("Car " + car.getId() + " from side number " + lane + " has started driving");

                            // anunta restul thread-urilor
                            intersection.notifyAll();
                        }
                    }
                }
            };
            default -> null;
        };
    }
}
