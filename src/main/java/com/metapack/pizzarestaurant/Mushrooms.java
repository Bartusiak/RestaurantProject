package com.metapack.pizzarestaurant;

public class Mushrooms extends PizzaDecorator {

    public Mushrooms(Pizza pizza) {
        super(pizza);
    }

    @Override
    public String typePizza() {
        return pizza.typePizza() + ", Mushrooms";
    }

    @Override
    public int getCost() {
        return pizza.getCost() + 2;
    }
}
