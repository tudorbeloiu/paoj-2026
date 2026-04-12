package com.pao.proiect.licitatii.service;

import com.pao.proiect.licitatii.model.products.Category;
import com.pao.proiect.licitatii.exception.CategoryNotExist;

import java.util.HashMap;

public class CategoryService {
    private static CategoryService instance;

    private HashMap<String, Category> categories;

    private CategoryService(){
        this.categories = new HashMap<>();
    }

    public static CategoryService getInstance(){
        if(instance == null){
            instance = new CategoryService();
        }
        return instance;
    }

    public Category findCategoryById(String id){
        if(!categories.containsKey(id)){
            throw new CategoryNotExist(id);
        }
        return categories.get(id);
    }

    public void printAllCategories(){
        for(Category category: categories.values()){
            System.out.println(category);
        }
    }
    public void addCategory(Category category){
        categories.put(category.getId(), category);
    }

    public HashMap<String ,Category> getCategories(){
        return new HashMap<>(categories);
    }
}
