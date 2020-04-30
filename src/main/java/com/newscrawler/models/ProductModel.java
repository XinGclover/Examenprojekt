package com.newscrawler.models;

import java.util.ArrayList;
import java.util.List;


import com.newscrawler.entity.Product;

public class ProductModel {

    public List<Product> findAll() {
        List<Product> products = new ArrayList<Product>();
        products.add(new Product("p01", "Product 1", 20, 5));
        products.add(new Product("p02", "Product 2", 21, 16));
        products.add(new Product("p03", "Product 3", 22, 3));
        products.add(new Product("p04", "Product 4", 23, 20));
        products.add(new Product("p05", "Product 5", 24, 8));
        return products;
    }

}
