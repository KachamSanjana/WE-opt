package com.dtzi.app;

public class Player {
   Gear gear;
   Player() {
     this.setGear(new Gear());
   }
   void setGear(Gear gear) {
     this.gear = gear;
   }
   Gear getGear() {
     return this.gear;
   }
}
