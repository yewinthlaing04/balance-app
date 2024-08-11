package com.ye.ecommerce.service;

import com.ye.ecommerce.dto.AddProductDto;
import com.ye.ecommerce.dto.UpdateProductDto;
import com.ye.ecommerce.model.Product;
import org.springframework.stereotype.Service;

import java.util.List;


public interface IProductService {

    // add product
    Product addProduct(AddProductDto addProductDto);

    // list all product
    List<Product> getAllProducts();

    // Product by id
    Product getProductById(Long id) ;

    // delete product
    void deleteProductById(Long id);

    // update product
    Product updateProduct(UpdateProductDto updateProductDto , Long productId);

    // list product with category
    List<Product> getProductByCategory(String category) ;

    // list product with brand
    List<Product> getProductByBrand(String brand);

    // list product with brand and category
    List<Product> getProductByCategoryAndBrand(String category, String brand) ;

    // get product by name
    List<Product> getProductByName(String name);

    // get product by name and brand
    List<Product> getProductByBrandAndName(String brand, String name) ;

    // count product
    Long countProductByBrandAndName(String brand , String name);
}
