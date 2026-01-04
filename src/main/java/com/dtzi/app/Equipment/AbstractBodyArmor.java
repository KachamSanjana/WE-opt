package com.dtzi.app.Equipment;

import java.rmi.NotBoundException;

public abstract class AbstractBodyArmor {
  int stat;
  String statType;
  public int getStat() {
    return stat;
  }
  public String getStatType() {
    return statType;
  }
}
