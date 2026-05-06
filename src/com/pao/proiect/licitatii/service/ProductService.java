package com.pao.proiect.licitatii.service;

import com.pao.proiect.licitatii.repository.ProductRepository;
import com.pao.proiect.licitatii.model.entities.Seller;
import com.pao.proiect.licitatii.model.products.Category;
import com.pao.proiect.licitatii.model.products.DigitalProduct;
import com.pao.proiect.licitatii.model.products.PhysicalProduct;
import com.pao.proiect.licitatii.model.products.Product;
import com.pao.proiect.licitatii.exception.ProductNotExist;

import java.util.HashMap;

public class ProductService {
    private final AuditService auditService = AuditService.getInstance();
    private static ProductService instance;
    private final ProductRepository productRepository = ProductRepository.getInstance();

    private HashMap<String, Product> products;

    private ProductService(){
        productRepository.initCounter();
        
        this.products = new HashMap<>();
        for(Product p: productRepository.readAll()){
            products.put(p.getId(), p);
        }
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
        auditService.log("Find product by id");
        return products.get(id);
    }
    public void printAllProducts(){
        for(Product product: products.values()){
            System.out.println(product);
        }
        auditService.log("Show all products");
    }

    public void addProduct(Product product, Seller seller){
        products.put(product.getId(), product);
        seller.addProduct(product);

        productRepository.create(product);

        if(product instanceof PhysicalProduct){
            auditService.log("Add physical product");
        }
        else if(product instanceof DigitalProduct){
            auditService.log("Add digital product");
        }
    }

    public void updatePhysicalProduct(String id, String name, String description, double price, Category category, int year, double weight){
        Product product = findProductById(id);
        if(!(product instanceof PhysicalProduct physicalProduct)){
            throw new ProductNotExist(id);
        }

        physicalProduct.setName(name);
        physicalProduct.setDescription(description);
        physicalProduct.setPrice(price);
        physicalProduct.setCategory(category);
        physicalProduct.setYear(year);
        physicalProduct.setWeight(weight);

        productRepository.update(physicalProduct);
        auditService.log("Update physical product");
    }

    public void updateDigitalProduct(String id, String name, String description, double price, Category category, String format, String licenseKey){
        Product product = findProductById(id);
        if(!(product instanceof DigitalProduct digitalProduct)){
            throw new ProductNotExist(id);
        }

        digitalProduct.setName(name);
        digitalProduct.setDescription(description);
        digitalProduct.setPrice(price);
        digitalProduct.setCategory(category);
        digitalProduct.setFormat(format);
        digitalProduct.setLicenseKey(licenseKey);

        productRepository.update(digitalProduct);
        auditService.log("Update digital product");
    }

    public void deleteProduct(String id){
        if(!products.containsKey(id)){
            throw new ProductNotExist(id);
        }
        products.remove(id);
        productRepository.delete(id);
        auditService.log("Delete product");
    }

    public HashMap<String, Product> getProducts(){
        return new HashMap<>(products);
    }
}
