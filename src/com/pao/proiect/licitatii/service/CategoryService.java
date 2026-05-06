package com.pao.proiect.licitatii.service;

import com.pao.proiect.licitatii.model.products.Category;
import com.pao.proiect.licitatii.exception.CategoryNotExist;

import com.pao.proiect.licitatii.repository.CategoryRepository;

import java.util.HashMap;

public class CategoryService {
    private final AuditService auditService = AuditService.getInstance();
    private final CategoryRepository categoryRepository = CategoryRepository.getInstance();


    private static CategoryService instance;

    private HashMap<String, Category> categories;

    private CategoryService(){
        categoryRepository.initCounter();
        
        this.categories = new HashMap<>();
        for(Category c: categoryRepository.readAll()){
            categories.put(c.getId(), c);
        }
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
        auditService.log("Show all categories");
    }
    public void addCategory(Category category){
        categories.put(category.getId(), category);
        categoryRepository.create(category);
        auditService.log("Add new category");
    }

    public void updateCategory(String id, String newName, String newDescription){
        Category c = findCategoryById(id);
        c.setName(newName);
        c.setDescription(newDescription);
        categoryRepository.update(c);
        auditService.log("Update category");
    }
    public void deleteCategory(String id){
        if(!categories.containsKey(id)){
            throw new CategoryNotExist(id);
        }
        categories.remove(id);
        categoryRepository.delete(id);
        auditService.log("Delete category");
    }

    public HashMap<String ,Category> getCategories(){
        return new HashMap<>(categories);
    }
}
