package com.dtzi.app.Equipment;

public class Chest extends AbstractBodyArmor implements Equipment {
  private int stat;
  final private String statType = "armor";
  public Chest(int stat) {
    this.stat = stat;
  }
  public int getStat() {
    return this.stat;
  }
  public String getStatType() {
    return this.statType;
  }
}
