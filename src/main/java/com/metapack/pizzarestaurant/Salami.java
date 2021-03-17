package com.metapack.pizzarestaurant;

public class Salami extends FoodDecorator {

    public Salami(Food pizza) {
        super(pizza);
    }

    @Override
    public String foodName() {
        return food.foodName() + ", Salami";
    }

    @Override
    public int getCost() {
        return food.getCost() + 2;
    }
}
