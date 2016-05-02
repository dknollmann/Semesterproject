package com.semesterdomain.semesterprojekt;

import java.io.Serializable;
import java.util.ArrayList;

public class Shopping_List implements Serializable{

    private long list_id;
    private ArrayList<Product> myProducts = new ArrayList<>();
    private int sumPrice;
    private String name;
    //id of the owner of the shopping list
    private int fk_user;

    public Shopping_List(){
        this.fk_user = 1;
    }

    public int calcPrice(ArrayList<Product> products){
        sumPrice = 0;
        for (Product p: products) {
            this.sumPrice += p.getPrice();
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getFk_user() {
        return fk_user;
    }

    public void setFk_user(int fk_user) {
        this.fk_user = fk_user;
    }

    public long getList_id() {
        return list_id;
    }

    public void setList_id(long list_id) {
        this.list_id = list_id;
    }
}
