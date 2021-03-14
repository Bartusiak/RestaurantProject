package com.metapack.pizzarestaurant;

public class PizzaDecorator implements Pizza {

    Pizza pizza;

    public PizzaDecorator(Pizza pizza){
        this.pizza = pizza;
    }

    @Override
    public String typePizza() {
        return pizza.typePizza();
    }

    @Override
    public int getCost() {
        return pizza.getCost();
    }
}
