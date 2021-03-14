package com.metapack.pizzarestaurant;

public class Salami extends PizzaDecorator {

    public Salami(Pizza pizza) {
        super(pizza);
    }

    @Override
    public String typePizza() {
        return pizza.typePizza() + ", Salami";
    }

    @Override
    public int getCost() {
        return pizza.getCost() + 2;
    }
}
