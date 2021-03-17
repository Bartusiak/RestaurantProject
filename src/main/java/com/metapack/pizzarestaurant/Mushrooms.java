package com.metapack.pizzarestaurant;

public class Mushrooms extends FoodDecorator {

    public Mushrooms(Food pizza) {
        super(pizza);
    }

    @Override
    public String foodName() {
        return food.foodName() + ", Mushrooms";
    }

    @Override
    public int getCost() {
        return food.getCost() + 2;
    }
}
