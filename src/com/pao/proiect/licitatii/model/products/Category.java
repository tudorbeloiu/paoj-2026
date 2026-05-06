package com.pao.proiect.licitatii.model.products;

import java.util.Objects;

public class Category {

    private static int contorId = 0;

    private final String id;
    private String name;
    private String description;

    public Category(String name, String description){
        this.id = "C-" + (++contorId);
        this.name = name;
        this.description = description;
    }
    public Category(String id, String name, String description){
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public static void initCounter(int max){
        contorId = max;
    }

    public String getId(){
        return id;
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

    @Override
    public String toString(){
        return "[" + id + "] " + name + " | " + description;
    }

    @Override
    public boolean equals(Object o){
        if(this == o)
            return true;
        if(o == null || getClass() != o.getClass()){
            return false;
        }
        Category c = (Category) o;
        return Objects.equals(id, c.id);
    }
    @Override
    public int hashCode(){
        return Objects.hash(id);
    }

}
