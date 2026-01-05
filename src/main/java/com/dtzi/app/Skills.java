package com.dtzi.app;

import java.util.HashMap;
import java.util.Map;

public class Skills {
  final Map<String, Number> BASE_STATS;
  Map<String, Number> stats = new HashMap<String, Number>();
  Map<String, Number> statIncrements = new HashMap<String, Number>() {
    {
      put("attackDamage", 20);
      put("criticalRate", 0.05f);
      put("criticalDamage", 0.2f);
      put("armor", 0.04f);
      put("precision", 0.05f);
      put("dodge", 0.04f);
      put("health", 1);
      put("lootChance", 0.01f);
      put("hunger", 1);
    }
  };
  int skillPoints;

  public Skills(int sp, Map<String, Number> stats) {
    this.skillPoints = sp;
    this.BASE_STATS = stats;
    this.stats = new HashMap<>(this.BASE_STATS);
  }
}
