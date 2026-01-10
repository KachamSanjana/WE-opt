package com.dtzi.app;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiPredicate;
import java.util.function.BinaryOperator;

public class StatsMaps {

  private static Map<String, Float> operate(Map<String, Float> map1, BinaryOperator<Float> op, Map<String, Float> map2) {
    Map<String, Float> sumMap = new HashMap<>();
    for (String elem : map1.keySet()) {
      switch (elem) {
        case "attackDamage", "health", "hunger", "criticalDamage", "production", "companies", "entre", "energy":
          sumMap.put(elem, op.apply(map1.get(elem), map2.get(elem)));
          break;
        case "armor":
          sumMap.put(elem,
              (float) Math.min(0.9, op.apply(map1.get(elem), map2.get(elem))));
          break;
        default:
          sumMap.put(elem,
              (float) Math.min(1, op.apply(map1.get(elem), map2.get(elem))));
      }
    }
    return sumMap;
  }

  static Map<String, Float> addMaps(Map<String, Float> map1, Map<String, Float> map2) {
    return operate(map1, Float::sum, map2);
  }

  static Map<String, Float> subtractMaps(Map<String, Float> map1, Map<String, Float> map2) {
    return operate(map1, (a, b) -> a - b, map2);
  }

  static <T extends Number> boolean compareAllWith(Map<String, T> map, BiPredicate<T, T> op, T number) {
    return map.values().stream().allMatch(v->op.test(v, number));
  }

  static <T extends Number & Comparable<T>> boolean allLargerThan(Map<String, T> map, T value) {
    return compareAllWith(map, (a, b) -> a.compareTo(b) > 0, value);
  }

  static <T extends Number & Comparable<T>> boolean allSmallerThan(Map<String, T> map, T value) {
    return compareAllWith(map, (a, b) -> a.compareTo(b) < 0, value);
  }

  static Map<String, Integer> subtractAll(Map<String,Integer> map, int num) {
    map.replaceAll((_, value) -> value - num);
    return map;
  }

  static Map<String, Float> subtractAll(Map<String,Float> map, float num) {
    map.replaceAll((_, value) -> value - num);
    return map;
  }
}
