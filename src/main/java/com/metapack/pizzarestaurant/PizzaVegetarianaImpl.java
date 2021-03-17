package com.metapack.pizzarestaurant;

public class PizzaVegetarianaImpl implements Food {
    @Override
    public String foodName() {
        return "Vegetariana";
    }

    @Override
    public int getCost() {
        return 22;
    }
}
