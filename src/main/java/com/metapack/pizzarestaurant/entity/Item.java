package com.metapack.pizzarestaurant.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Item {

    @SerializedName("foodName")
    public String foodName;
    @SerializedName("foodPrice")
    public int foodPrice;
    @SerializedName("amount")
    public int amount;

    public Item(String foodName, int foodPrice, int amount){
        this.foodName = foodName;
        this.foodPrice = foodPrice;
        this.amount = amount;
    }

    public int getSum() {
        return foodPrice * amount;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public int getFoodPrice() {
        return foodPrice;
    }

    public void setFoodPrice(int foodPrice) {
        this.foodPrice = foodPrice;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
