package com.semesterdomain.semesterprojekt;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by L 875 on 26.04.2016.
 */

public class Shopping_List implements Serializable{

    private ArrayList<Product> myProducts = new ArrayList<Product>();
    private int sumPrice;
    private String name;

    public Shopping_List(){

    }

    public int calcPrice(){
        sumPrice = 0;
        for (Product p: myProducts) {
            this.sumPrice += p.price;
        }
        return this.sumPrice;
    }

    public ArrayList<Product> getMyProducts() {
        return myProducts;
    }

    public void setMyProducts(ArrayList<Product> myProducts) {
        this.myProducts = myProducts;
    }

    public int getSumPrice() {
        return sumPrice;
    }

    public void setSumPrice(int sumPrice) {
        this.sumPrice = sumPrice;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
