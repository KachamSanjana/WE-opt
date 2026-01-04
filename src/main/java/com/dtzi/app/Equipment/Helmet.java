package com.dtzi.app.Equipment;

public class Helmet extends AbstractBodyArmor implements Equipment {
  private int stat;
  final private String statType = "criticalDamage";
  public Helmet(int stat) {
    this.stat = stat;
  }
  public int getStat() {
    return this.stat;
  }
  public String getStatType() {
    return this.statType;
  }
}
