package com.ecommerce.productservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@SpringBootApplication
@RestController
@CrossOrigin(origins = "*")
public class ProductServiceApplication {

    private final List<Product> products = new ArrayList<>();
    private Long nextId = 1L;

    public static void main(String[] args) {
        SpringApplication.run(ProductServiceApplication.class, args);
    }

    // Initialize sample data
    public ProductServiceApplication() {
        initializeProducts();
    }

    private void initializeProducts() {
        products.add(new Product(nextId++, "iPhone 15 Pro Max", new BigDecimal("1199.00"), "smartphones", 
            "Latest iPhone with titanium design and A17 Pro chip", "Apple", 15));
        products.add(new Product(nextId++, "MacBook Pro M3", new BigDecimal("1999.00"), "laptops", 
            "Powerful laptop with M3 chip for professionals", "Apple", 8));
        products.add(new Product(nextId++, "AirPods Pro 2", new BigDecimal("249.00"), "audio", 
            "Premium wireless earbuds with spatial audio", "Apple", 25));
        products.add(new Product(nextId++, "iPad Pro 12.9", new BigDecimal("1099.00"), "tablets", 
            "Professional tablet with M2 chip", "Apple", 12));
        products.add(new Product(nextId++, "Samsung Galaxy S24 Ultra", new BigDecimal("1199.00"), "smartphones", 
            "Android flagship with S Pen and AI features", "Samsung", 18));
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "healthy");
        response.put("service", "product-service");
        response.put("timestamp", LocalDateTime.now());
        response.put("productsCount", products.size());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/products")
    public ResponseEntity<Map<String, Object>> getAllProducts(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String search) {
        
        List<Product> filteredProducts = products;
        
        if (category != null && !category.isEmpty()) {
            filteredProducts = products.stream()
                .filter(p -> p.getCategory().equalsIgnoreCase(category))
                .toList();
        }
        
        if (search != null && !search.isEmpty()) {
            filteredProducts = filteredProducts.stream()
                .filter(p -> p.getName().toLowerCase().contains(search.toLowerCase()) ||
                           p.getDescription().toLowerCase().contains(search.toLowerCase()))
                .toList();
        }
        
        Map<String, Object> response = new HashMap<>();
        response.put("products", filteredProducts);
        response.put("count", filteredProducts.size());
        response.put("total", products.size());
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<Map<String, Object>> getProduct(@PathVariable Long id) {
        Optional<Product> product = products.stream()
            .filter(p -> p.getId().equals(id))
            .findFirst();
            
        if (product.isPresent()) {
            Map<String, Object> response = new HashMap<>();
            response.put("product", product.get());
            return ResponseEntity.ok(response);
        } else {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "Product not found");
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/categories")
    public ResponseEntity<Map<String, Object>> getCategories() {
        Set<String> categories = new HashSet<>();
        products.forEach(p -> categories.add(p.getCategory()));
        
        Map<String, Object> response = new HashMap<>();
        response.put("categories", categories);
        response.put("count", categories.size());
        
        return ResponseEntity.ok(response);
    }
}

// Product Entity
class Product {
    private Long id;
    private String name;
    private BigDecimal price;
    private String category;
    private String description;
    private String brand;
    private Integer stock;
    private LocalDateTime createdAt;

    public Product(Long id, String name, BigDecimal price, String category, 
                  String description, String brand, Integer stock) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
        this.description = description;
        this.brand = brand;
        this.stock = stock;
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }
    
    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}