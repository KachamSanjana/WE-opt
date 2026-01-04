package com.dtzi.app.Equipment;

public class Gloves extends AbstractBodyArmor implements Equipment {
  private int stat;
  final private String statType = "precision";
  public Gloves (int stat) {
    this.stat = stat;
  }
  public int getStat() {
    return this.stat;
  }
  public String getStatType() {
    return this.statType;
  }
}
