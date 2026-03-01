package com.example.demo.application.port.in;

import com.example.demo.domain.model.Product;
import java.util.List;

public interface ListProductsUseCase {

    List<Product> listProducts();
}
