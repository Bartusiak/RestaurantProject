package com.metapack.pizzarestaurant;

public class FoodDecorator implements Food {

    Food food;

    public FoodDecorator(Food food){
        this.food = food;
    }

    @Override
    public String foodName() {
        return food.foodName();
    }

    @Override
    public int getCost() {
        return food.getCost();
    }
}
