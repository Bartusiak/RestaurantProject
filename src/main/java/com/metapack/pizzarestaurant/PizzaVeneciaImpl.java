package com.metapack.pizzarestaurant;

public class PizzaVeneciaImpl implements Food {
    @Override
    public String foodName() {
        return "Venecia";
    }

    @Override
    public int getCost() {
        return 25;
    }
}
