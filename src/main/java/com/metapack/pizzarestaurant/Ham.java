package com.metapack.pizzarestaurant;

public class Ham extends FoodDecorator {

    public Ham(Food pizza) {
        super(pizza);
    }

    @Override
    public String foodName() {
        return food.foodName() + ", Ham";
    }

    @Override
    public int getCost() {
        return food.getCost() + 2;
    }
}
