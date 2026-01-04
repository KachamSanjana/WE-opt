package com.dtzi.app.Equipment;
 
public class Boots extends AbstractBodyArmor implements Equipment {
  private int stat;
  final private String statType = "dodge";
  public Boots(int stat) {
    this.stat = stat;
  }
  public int getStat() {
    return this.stat;
  }
  public String getStatType() {
    return this.statType;
  }
}
