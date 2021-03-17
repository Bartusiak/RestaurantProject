package com.metapack.pizzarestaurant;

public class DoubleCheese extends FoodDecorator {

    public DoubleCheese(Food pizza) {
        super(pizza);
    }

    @Override
    public String foodName() {
        return food.foodName() + ", Double Cheese";
    }

    @Override
    public int getCost() {
        return food.getCost() + 2;
    }
}
