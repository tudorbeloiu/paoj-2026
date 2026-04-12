package com.pao.proiect.licitatii.model.products;

import com.pao.proiect.licitatii.exception.InvalidYear;
import com.pao.proiect.licitatii.exception.InvalidNumber;

import java.time.LocalDateTime;

public class PhysicalProduct extends Product{

    private int year;
    private double weight;

    public PhysicalProduct(String name, String description, double price, Category category, int year, double weight){
        super(name, description, price, category);

        if(year > LocalDateTime.now().getYear()){
            throw new InvalidYear(year);
        }
        if(weight <= 0){
            throw new InvalidNumber();
        }
        this.year = year;
        this.weight = weight;
    }

    @Override
    public String getReceiveInfo(){
        return "[Year]: " + year + " | [Weight]: " + weight;
    }
    @Override
    public String toString(){
        return super.toString() + " | " + getReceiveInfo();
    }

    public int getYear(){
        return year;
    }
    public double getWeight(){
        return weight;
    }

    public void setYear(int year){
        if(year > LocalDateTime.now().getYear()){
            throw new InvalidYear(year);
        }
        this.year = year;
    }

    public void setWeight(double weight){
        if(weight <= 0){
            throw new InvalidNumber();
        }
        this.weight = weight;
    }


}
