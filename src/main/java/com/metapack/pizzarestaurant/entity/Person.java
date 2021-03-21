package com.metapack.pizzarestaurant.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Person {

    @SerializedName("email")
    public String email;

    @SerializedName("phone")
    public String phone;

    @SerializedName("Food")
    public List<com.metapack.pizzarestaurant.entity.Item> foods;

    public Person(String email, String phone, List<com.metapack.pizzarestaurant.entity.Item> foods){
        this.email = email;
        this.phone = phone;
        this.foods = foods;
    }

    public List<Item> getFoods() {        return foods;
    }

    public void setFoods(List<Item> foods) {
        this.foods = foods;
    }
}
