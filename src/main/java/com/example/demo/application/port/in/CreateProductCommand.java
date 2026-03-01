package com.example.demo.application.port.in;

import java.math.BigDecimal;

public record CreateProductCommand(String name, BigDecimal price) {
}
