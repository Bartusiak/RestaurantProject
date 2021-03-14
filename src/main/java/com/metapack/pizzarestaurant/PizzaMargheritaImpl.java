package com.metapack.pizzarestaurant;

public class PizzaMargheritaImpl implements Pizza {
    @Override
    public String typePizza() {
        return "Margherita";
    }

    @Override
    public int getCost() {
        return 20;
    }
}
