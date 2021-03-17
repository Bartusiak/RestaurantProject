package com.metapack.pizzarestaurant;

public class PizzaToscaImpl implements Food {

    @Override
    public String foodName() {
        return "Tosca";
    }

    @Override
    public int getCost() {
        return 25;
    }
}
