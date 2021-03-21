package com.metapack.pizzarestaurant.entity;

import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvCustomBindByName;
import com.opencsv.bean.CsvCustomBindByPosition;

public class ProductParse {

    @CsvBindByPosition(position = 0)
    private String id;

    @CsvBindByPosition(position = 1)
    private String itemVal;

    @CsvBindByPosition(position = 2)
    private String priceVal;

    @CsvBindByPosition(position = 3)
    private String amount;

    @CsvBindByPosition(position = 4)
    private String totalVal;

    @CsvBindByPosition(position = 5)
    private String email;

    @CsvBindByPosition(position = 6)
    private String phone;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getItemVal() {
        return itemVal;
    }

    public void setItemVal(String itemVal) {
        this.itemVal = itemVal;
    }

    public String getPriceVal() {
        return priceVal;
    }

    public void setPriceVal(String priceVal) {
        this.priceVal = priceVal;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTotalVal() {
        return totalVal;
    }

    public void setTotalVal(String totalVal) {
        this.totalVal = totalVal;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "ProductParse{" +
                "id='" + id + '\'' +
                ", itemVal='" + itemVal + '\'' +
                ", priceVal='" + priceVal + '\'' +
                ", amount='" + amount + '\'' +
                ", totalVal='" + totalVal + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
