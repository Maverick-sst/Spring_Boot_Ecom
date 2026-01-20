package com.example.ecommerce.service;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ecommerce.dto.AddToCartRequest;
import com.example.ecommerce.model.CartItem;
import com.example.ecommerce.repository.CartRepository;

@Service
public class CartService{
    @Autowired
    private CartRepository cartRepository;

    public CartItem addToCart(AddToCartRequest request){
        CartItem cartItem = new CartItem();
        cartItem.setUserId(request.getUserId());
        cartItem.setProductId(request.getProductId());
        cartItem.setQuantity(request.getQuantity());

        return cartRepository.save(cartItem);
    }

    public List<CartItem> getCartItems(String userId){
        return cartRepository.findByUserId(userId);
    }

    public Map<String, String> clearCart(String userId){
        List<CartItem> items = cartRepository.findByUserId(userId);
        cartRepository.deleteAll(items);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Cart cleared successfully");
        return response;
    }
}