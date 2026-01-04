package com.dtzi.app.Equipment;

public class Pants extends AbstractBodyArmor implements Equipment {
  private int stat;
  final private String statType = "armor";
  public Pants(int stat) {
    this.stat = stat;
  }
  public int getStat() {
    return this.stat;
  }
  public String getStatType() {
    return this.statType;
  }
}
