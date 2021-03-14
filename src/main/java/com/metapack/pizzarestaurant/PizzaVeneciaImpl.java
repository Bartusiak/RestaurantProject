package com.metapack.pizzarestaurant;

public class PizzaVeneciaImpl implements Pizza {
    @Override
    public String typePizza() {
        return "Venecia";
    }

    @Override
    public int getCost() {
        return 25;
    }
}
