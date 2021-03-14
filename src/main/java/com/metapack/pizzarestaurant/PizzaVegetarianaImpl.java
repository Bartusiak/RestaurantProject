package com.metapack.pizzarestaurant;

public class PizzaVegetarianaImpl implements Pizza {
    @Override
    public String typePizza() {
        return "Vegetariana";
    }

    @Override
    public int getCost() {
        return 22;
    }
}
