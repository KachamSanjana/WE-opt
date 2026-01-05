package com.dtzi.app;

import java.util.HashMap;
import java.util.Map;

public class StatsMaps {

  static Map<String, Float> addMaps(Map<String, Float> map1, Map<String, Float> map2) {
    Map<String, Float> sumMap = new HashMap<>();
    for (String elem : map1.keySet()) {
      switch (elem) {
        case "attackDamage", "health", "hunger", "criticalDamage":
          sumMap.put(elem, map1.get(elem) + map2.get(elem));
          break;
        case "armor":
          sumMap.put(elem,
              (float) Math.min(0.9, map1.get(elem) + map2.get(elem)));
          break;
        default:
          sumMap.put(elem,
              (float) Math.min(1, map1.get(elem) + map2.get(elem)));
      }
    }
    return sumMap;
  }

  static <T extends Number> boolean allLargerThan(Map<String, T> map, T value) {
    int counter = 0;
    for (T element : map.values()) {
      if (element.floatValue() > value.floatValue()) {
        counter++;
      }
    }
    if (counter == map.size()) {
      return true;
    }
    return false;
  }

  static <T extends Number> boolean allSmallerThan(Map<String, T> map, T value) {
    int counter = 0;
    for (T element : map.values()) {
      if (element.floatValue() < value.floatValue()) {
        counter++;
      }
    }
    if (counter == map.size()) {
      return true;
    }
    return false;
  }

  static Map<String, Integer> subtractAll(Map<String,Integer> map, int num) {
    for (String key : map.keySet()) {
      map.put(key, map.get(key) - num);
    }
    return map;
  }

  static Map<String, Float> subtractAll(Map<String, Float> map, float num) {
    for (String key : map.keySet()) {
      map.put(key, map.get(key) - num);
    }
    return map;
  }

}
