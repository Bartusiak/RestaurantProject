package com.metapack.pizzarestaurant;

public class SchabBuilder {

    private String name;
    private int price;

    SchabBuilder name(String name){
        this.name = name;
        return this;
    }

    SchabBuilder price(int price){
        this.price = price;
        return this;
    }

    Schab build(){
        return new Schab(name,price);
    }

}
