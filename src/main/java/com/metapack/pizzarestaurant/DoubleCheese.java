package com.metapack.pizzarestaurant;

public class DoubleCheese extends PizzaDecorator {

    public DoubleCheese(Pizza pizza) {
        super(pizza);
    }

    @Override
    public String typePizza() {
        return pizza.typePizza() + ", Double Cheese";
    }

    @Override
    public int getCost() {
        return pizza.getCost() + 2;
    }
}
