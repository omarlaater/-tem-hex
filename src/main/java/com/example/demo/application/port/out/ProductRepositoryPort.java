package com.example.demo.application.port.out;

import com.example.demo.domain.model.Product;
import java.util.List;

public interface ProductRepositoryPort {

    Product save(Product product);

    List<Product> findAll();
}
