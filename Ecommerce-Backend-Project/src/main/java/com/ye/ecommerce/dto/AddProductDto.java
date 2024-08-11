package com.ye.ecommerce.dto;

import com.ye.ecommerce.model.Category;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AddProductDto {

    private String name;
    private String brand;
    private BigDecimal price;
    private int inventory;
    private String description;
    private Category category;
}
