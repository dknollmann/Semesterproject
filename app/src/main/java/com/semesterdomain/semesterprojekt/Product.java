package com.semesterdomain.semesterprojekt;

import java.io.Serializable;

/**
 * Created by L 875 on 23.04.2016.
 */
public class Product implements Serializable{

    public String name;
    public String producer;
    public int price;



    public Product(String name, String producer, int price){
        this.name = name;
        this.producer = producer;
        this.price = price;
    }

}
