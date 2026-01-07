package com.dtzi.app.Buffs;

public record RegionalBonuses(float regionResistance, boolean coreRegion) {
  public float bonus() {
    return regionResistance + (coreRegion ? 0.15f : 0f);
  }
}
