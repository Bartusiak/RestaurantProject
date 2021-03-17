package com.metapack.pizzarestaurant;

public class Schab {

    private String name;
    private int price;

    Schab(String name, int price){
        this.name = name;
        this.price = price;
    }

    static SchabBuilder builder(){
        return new SchabBuilder();
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "Schab{" +
                "name='" + name + '\'' +
                ", price=" + price +
                '}';
    }
}
