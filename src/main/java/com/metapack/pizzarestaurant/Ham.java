package com.metapack.pizzarestaurant;

public class Ham extends PizzaDecorator {

    public Ham(Pizza pizza) {
        super(pizza);
    }

    @Override
    public String typePizza() {
        return pizza.typePizza() + ", Ham";
    }

    @Override
    public int getCost() {
        return pizza.getCost() + 2;
    }
}
