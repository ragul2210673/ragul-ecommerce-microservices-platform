package com.ecommerce.cartservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;

@SpringBootApplication
@RestController
@CrossOrigin(origins = "*")
public class CartServiceApplication {

    private final Map<Long, Cart> carts = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        SpringApplication.run(CartServiceApplication.class, args);
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "healthy");
        response.put("service", "cart-service");
        response.put("timestamp", LocalDateTime.now());
        response.put("activeCarts", carts.size());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/cart/{userId}")
    public ResponseEntity<Map<String, Object>> getCart(@PathVariable Long userId) {
        Cart cart = carts.computeIfAbsent(userId, k -> new Cart(k));
        
        Map<String, Object> response = new HashMap<>();
        response.put("cart", toCartResponse(cart));
        
        return ResponseEntity.ok(response);
    }

    @PostMapping("/cart/{userId}/items")
    public ResponseEntity<Map<String, Object>> addToCart(
            @PathVariable Long userId,
            @RequestBody AddToCartRequest request) {
        
        if (request.getProductId() == null || request.getQuantity() <= 0) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "Invalid product ID or quantity");
            return ResponseEntity.badRequest().body(response);
        }

        Cart cart = carts.computeIfAbsent(userId, k -> new Cart(k));
        
        Optional<CartItem> existingItem = cart.getItems().stream()
            .filter(item -> item.getProductId().equals(request.getProductId()))
            .findFirst();
            
        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + request.getQuantity());
        } else {
            CartItem newItem = new CartItem(
                request.getProductId(),
                request.getProductName(),
                request.getPrice(),
                request.getQuantity()
            );
            cart.getItems().add(newItem);
        }
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Item added to cart successfully");
        response.put("cart", toCartResponse(cart));
        
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/cart/{userId}")
    public ResponseEntity<Map<String, Object>> clearCart(@PathVariable Long userId) {
        Cart cart = carts.get(userId);
        if (cart != null) {
            cart.getItems().clear();
        }
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Cart cleared successfully");
        
        return ResponseEntity.ok(response);
    }

    private CartResponse toCartResponse(Cart cart) {
        BigDecimal subtotal = cart.getItems().stream()
            .map(item -> item.getPrice().multiply(new BigDecimal(item.getQuantity())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        BigDecimal tax = subtotal.multiply(new BigDecimal("0.08"));
        BigDecimal total = subtotal.add(tax);
        
        int itemCount = cart.getItems().stream()
            .mapToInt(CartItem::getQuantity)
            .sum();
        
        return new CartResponse(cart.getUserId(), cart.getItems(), itemCount, subtotal, tax, total);
    }
}

// Cart Entity
class Cart {
    private Long userId;
    private List<CartItem> items;

    public Cart(Long userId) {
        this.userId = userId;
        this.items = new ArrayList<>();
    }

    public Long getUserId() { return userId; }
    public List<CartItem> getItems() { return items; }
}

// Cart Item Entity
class CartItem {
    private Long productId;
    private String productName;
    private BigDecimal price;
    private Integer quantity;

    public CartItem(Long productId, String productName, BigDecimal price, Integer quantity) {
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
    }

    // Getters and Setters
    public Long getProductId() { return productId; }
    public String getProductName() { return productName; }
    public BigDecimal getPrice() { return price; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
}

// DTOs
class CartResponse {
    private Long userId;
    private List<CartItem> items;
    private Integer itemCount;
    private BigDecimal subtotal;
    private BigDecimal tax;
    private BigDecimal total;

    public CartResponse(Long userId, List<CartItem> items, Integer itemCount,
                       BigDecimal subtotal, BigDecimal tax, BigDecimal total) {
        this.userId = userId;
        this.items = items;
        this.itemCount = itemCount;
        this.subtotal = subtotal;
        this.tax = tax;
        this.total = total;
    }

    // Getters
    public Long getUserId() { return userId; }
    public List<CartItem> getItems() { return items; }
    public Integer getItemCount() { return itemCount; }
    public BigDecimal getSubtotal() { return subtotal; }
    public BigDecimal getTax() { return tax; }
    public BigDecimal getTotal() { return total; }
}

class AddToCartRequest {
    private Long productId;
    private String productName;
    private BigDecimal price;
    private Integer quantity;

    // Getters and Setters
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
}