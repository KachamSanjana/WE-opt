package com.dtzi.app.Buffs;

public record PoliticalBonuses(boolean ally, boolean swornEnemy) {
  public float bonus() {
    return (ally ? 0.1f : 0) + (swornEnemy ? 0.1f : 0);
  }
}
