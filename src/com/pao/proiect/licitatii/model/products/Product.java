package com.pao.proiect.licitatii.model.products;

import com.pao.proiect.licitatii.exception.InvalidNumber;

public abstract class Product {

    private double price;
    private Category category;

    private static int contorId = 0;
    private final String id;

    private String name;
    private String description;


    public Product(String name, String description, double price, Category category){
        if(price < 0){
            throw new InvalidNumber();
        }
        this.id = "P-" + (++contorId);

        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
    }
    public Product(String id, String name, String description, double price, Category category){
        this.id = id;

        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
    }

    public static void initCounter(int max){
        contorId = max;
    }

    public abstract String getReceiveInfo();


    public String getId(){
        return id;
    }
    public double getPrice(){
        return price;
    }
    public Category getCategory(){
        return category;
    }
    public String getName(){
        return name;
    }
    public String getDescription(){
        return description;
    }


    public void setName(String name){
        this.name = name;
    }
    public void setDescription(String description){
        this.description = description;
    }
    public void setPrice(double price){
        if(price > 0){
            this.price = price;
        }
    }
    public void setCategory(Category category){
        this.category = category;
    }

    @Override
    public String toString(){
        return "[" + id + "] " + name + " | " + description + " | Price: " + price;
    }
}
