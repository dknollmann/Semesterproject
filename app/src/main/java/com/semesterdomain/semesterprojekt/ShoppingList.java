package com.semesterdomain.semesterprojekt;

import java.io.Serializable;
import java.util.ArrayList;

public class ShoppingList implements Serializable {

    private long listId;
    private ArrayList<Product> myProducts = new ArrayList<>();
    private int sumPrice;
    private int budget;
    private String name;
    //id of the owner of the shopping list
    private int fkUser;

    public ShoppingList() {
        this.fkUser = 1;
        sumPrice = 0;
        myProducts = new ArrayList<>();
        this.budget = 0;
    }

    public int calcPrice(ArrayList<Product> products) {
        sumPrice = 0;
        for (Product p : products) {
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

    public int getFkUser() {
        return fkUser;
    }

    public void setFkUser(int fk_user) {
        this.fkUser = fk_user;
    }

    public long getListId() {
        return listId;
    }

    public void setListId(long list_id) {
        this.listId = list_id;
    }

    public int getBudget() {
        return budget;
    }

    public void setBudget(int budget) {
        this.budget = budget;
    }

}
