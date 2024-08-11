package com.ye.ecommerce.service.impl;

import com.ye.ecommerce.dto.AddProductDto;
import com.ye.ecommerce.dto.UpdateProductDto;
import com.ye.ecommerce.exception.ProductNotFoundException;
import com.ye.ecommerce.model.Category;
import com.ye.ecommerce.model.Product;
import com.ye.ecommerce.repo.CategoryRepository;
import com.ye.ecommerce.repo.ProductRepository;
import com.ye.ecommerce.service.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {

    private final ProductRepository productRepository;

    private final CategoryRepository categoryRepository;

    @Override
    public Product addProduct(AddProductDto addProductDto) {

        // check if the category is found in the DB
        // if yes , add as new product category
        // if no , save it
        // set as the new product category

        Category category = Optional.ofNullable(
                categoryRepository.findByName(addProductDto.getCategory().getName()))
                .orElseGet(() -> {
                    Category newCategory = new Category(addProductDto.getCategory().getName());
                    return categoryRepository.save(newCategory);
                });
        addProductDto.setCategory(category);

        return productRepository.save(createProduct( addProductDto , category));
    }

    private Product createProduct(AddProductDto addProductDto , Category category) {

        return new Product (
                addProductDto.getName(),
                addProductDto.getBrand(),
                addProductDto.getPrice(),
                addProductDto.getInventory(),
                addProductDto.getDescription(),
                category
        );
    }


    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow( () -> new ProductNotFoundException("Product not Found"));
    }

    @Override
    public void deleteProductById(Long id) {
        productRepository.findById(id)
                .ifPresentOrElse(productRepository::delete
                        , () -> { throw new ProductNotFoundException("Product not Found");});
    }

    @Override
    public Product updateProduct(UpdateProductDto updateProductdto, Long productId) {
        return productRepository.findById(productId)
                .map(existingProduct -> updateProduct( updateProductdto , existingProduct ))
                .map(productRepository::save)
                .orElseThrow( () -> new ProductNotFoundException("Product not Found"));
    }

    private Product updateProduct(UpdateProductDto updateProductDto, Product existingProduct){

        existingProduct.setName(updateProductDto.getName());
        existingProduct.setBrand(updateProductDto.getBrand());
        existingProduct.setPrice(updateProductDto.getPrice());
        existingProduct.setInventory(updateProductDto.getInventory());
        existingProduct.setDescription(updateProductDto.getDescription());

        Category category = categoryRepository.findByName(updateProductDto.getCategory().getName());

        existingProduct.setCategory(category);

        return existingProduct;
    }

    @Override
    public List<Product> getProductByCategory(String category) {
        return productRepository.findByCategoryName(category);
    }

    @Override
    public List<Product> getProductByBrand(String brand) {
        return productRepository.findByBrand(brand);
    }

    @Override
    public List<Product> getProductByCategoryAndBrand(String category, String brand) {
        return productRepository.findByCategoryNameAndBrand(category , brand);
    }

    @Override
    public List<Product> getProductByName(String name) {
        return productRepository.findByName(name);
    }

    @Override
    public List<Product> getProductByBrandAndName(String brand, String name) {
        return productRepository.findByBrandAndName(brand , name);
    }

    @Override
    public Long countProductByBrandAndName(String brand, String name) {
        return productRepository.countByBrandAndName(brand,name);
    }
}
