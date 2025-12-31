package com.ecommerce.cartservice.service;

import com.ecommerce.cartservice.entity.CartItem;
import com.ecommerce.cartservice.repository.CartItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {
    
    private final CartItemRepository cartItemRepository;
    
    public List<CartItem> getCartItems(Long userId) {
        return cartItemRepository.findByUserId(userId);
    }
    
    @Transactional
    public CartItem addToCart(CartItem cartItem) {
        cartItem.setSubtotal(cartItem.getProductPrice()
                .multiply(BigDecimal.valueOf(cartItem.getQuantity())));
        
        var existingItem = cartItemRepository
                .findByUserIdAndProductId(cartItem.getUserId(), cartItem.getProductId());
        
        if (existingItem.isPresent()) {
            CartItem existing = existingItem.get();
            existing.setQuantity(existing.getQuantity() + cartItem.getQuantity());
            existing.setSubtotal(existing.getProductPrice()
                    .multiply(BigDecimal.valueOf(existing.getQuantity())));
            return cartItemRepository.save(existing);
        }
        
        return cartItemRepository.save(cartItem);
    }
    
    @Transactional
    public CartItem updateCartItem(Long id, Integer quantity) {
        CartItem cartItem = cartItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));
        cartItem.setQuantity(quantity);
        cartItem.setSubtotal(cartItem.getProductPrice()
                .multiply(BigDecimal.valueOf(quantity)));
        return cartItemRepository.save(cartItem);
    }
    
    @Transactional
    public void removeFromCart(Long id) {
        cartItemRepository.deleteById(id);
    }
    
    @Transactional
    public void clearCart(Long userId) {
        cartItemRepository.deleteByUserId(userId);
    }
    
    public BigDecimal getCartTotal(Long userId) {
        BigDecimal total = cartItemRepository.calculateCartTotal(userId);
        return total != null ? total : BigDecimal.ZERO;
    }
}