package com.metapack.pizzarestaurant.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Product {

   @SerializedName("Id")
   public String id;
   @SerializedName("Food")
   public List<com.metapack.pizzarestaurant.entity.Item> foods;

   public Product(String id, List<com.metapack.pizzarestaurant.entity.Item> foods) {
      this.id = id;
      this.foods = foods;
   }

}
