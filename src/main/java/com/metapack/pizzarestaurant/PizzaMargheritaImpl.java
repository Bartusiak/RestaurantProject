package com.metapack.pizzarestaurant;

public class PizzaMargheritaImpl implements Food {
    @Override
    public String foodName() {
        return "Margherita";
    }

    @Override
    public int getCost() {
        return 20;
    }
}
