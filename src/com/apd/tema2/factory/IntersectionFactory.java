package com.apd.tema2.factory;

import com.apd.tema2.entities.Intersection;
import com.apd.tema2.intersections.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Prototype Factory: va puteti crea cate o instanta din fiecare tip de implementare de Intersection.
 */
public class IntersectionFactory {
    private static Map<String, Intersection> cache = new HashMap<>();

    static {
        cache.put("simple_semaphore_intersection", new Intersection() {
        });
        cache.put("simple_n_roundabout_intersection", new SimpleRoundabout() {
        });
        cache.put("simple_strict_1_car_roundabout_intersection", new StrictRoundabout() {
        });
        cache.put("simple_strict_x_car_roundabout_intersection", new StrictRoundabout() {
        });
        cache.put("simple_max_x_car_roundabout_intersection", new StrictRoundabout() {
        });
        cache.put("priority_intersection", new PriorityIntersection() {
        });
        cache.put("crosswalk_intersection", new CrossWalkIntersection() {
        });
        cache.put("simple_maintenance_intersection", new SimpleMaintenance() {
        });
        cache.put("complex_maintenance_intersection", new ComplexMaintenance() {
        });
        cache.put("railroad_intersection", new RailRoad() {
        });

    }

    public static Intersection getIntersection(String handlerType) {
        return cache.get(handlerType);
    }

}
