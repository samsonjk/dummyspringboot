package com.axxohub.dummyspringboot.service;

import com.axxohub.dummyspringboot.dto.ProductRequest;
import com.axxohub.dummyspringboot.exception.BadRequestException;
import com.axxohub.dummyspringboot.exception.ResourceNotFoundException;
import com.axxohub.dummyspringboot.model.Product;
import com.axxohub.dummyspringboot.repository.ProductRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Product findById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + id));
    }

    public List<Product> findLowStock(int threshold) {
        return productRepository.findByStockQuantityLessThanEqual(threshold);
    }

    public Product create(ProductRequest request) {
        Product product = new Product();
        mapRequest(product, request);
        product.setCreatedAt(LocalDateTime.now());
        return productRepository.save(product);
    }

    public Product update(Long id, ProductRequest request) {
        Product product = findById(id);
        mapRequest(product, request);
        return productRepository.save(product);
    }

    public Product adjustStock(Long id, int delta) {
        Product product = findById(id);
        int updated = product.getStockQuantity() + delta;
        if (updated < 0) {
            throw new BadRequestException("Stock cannot become negative");
        }
        product.setStockQuantity(updated);
        return productRepository.save(product);
    }

    public void delete(Long id) {
        productRepository.delete(findById(id));
    }

    private void mapRequest(Product product, ProductRequest request) {
        product.setSku(request.sku());
        product.setName(request.name());
        product.setCategory(request.category());
        product.setPrice(request.price());
        product.setStockQuantity(request.stockQuantity());
        product.setActive(request.active());
    }
}
