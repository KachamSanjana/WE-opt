package com.dtzi.app.Buffs;

public record MilitaryUnitBonuses(float militaryUnitOrder, float militaryUnitHeadquarters) {
  public float bonus() {
    return militaryUnitHeadquarters + militaryUnitOrder;
  }
}
