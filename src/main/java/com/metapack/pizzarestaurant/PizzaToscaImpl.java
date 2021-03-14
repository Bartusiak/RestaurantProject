package com.metapack.pizzarestaurant;

public class PizzaToscaImpl implements Pizza {

    @Override
    public String typePizza() {
        return "Tosca";
    }

    @Override
    public int getCost() {
        return 25;
    }
}
