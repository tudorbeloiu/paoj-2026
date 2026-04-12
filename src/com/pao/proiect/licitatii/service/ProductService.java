package com.pao.proiect.licitatii.service;

import com.pao.proiect.licitatii.model.entities.Seller;
import com.pao.proiect.licitatii.model.products.Product;
import com.pao.proiect.licitatii.exception.ProductNotExist;

import java.util.HashMap;

public class ProductService {
    private static ProductService instance;

    private HashMap<String, Product> products;

    private ProductService(){
        this.products = new HashMap<>();
    }

    public static ProductService getInstance(){
        if(instance == null){
            instance = new ProductService();
        }
        return instance;
    }

    public Product findProductById(String id){
        if(!products.containsKey(id)){
            throw new ProductNotExist(id);
        }
        return products.get(id);
    }
    public void printAllProducts(){
        for(Product product: products.values()){
            System.out.println(product);
        }
    }

    public void addProduct(Product product, Seller seller){
        products.put(product.getId(), product);
        seller.addProduct(product);
    }
    public HashMap<String, Product> getProducts(){
        return new HashMap<>(products);
    }
}
