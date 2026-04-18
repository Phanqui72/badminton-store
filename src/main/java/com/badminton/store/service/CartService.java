package com.badminton.store.service;

import com.badminton.store.dto.ErrorCode;
import com.badminton.store.exception.NotFoundException;
import com.badminton.store.model.Cart;
import com.badminton.store.model.CartItem;
import com.badminton.store.model.User;
import com.badminton.store.repository.CartRepository;
import com.badminton.store.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class CartService {
    @Autowired private CartRepository cartRepository;
    @Autowired private UserRepository userRepository;

    public Cart getOrCreateCart(Long userId) {
        return cartRepository.findById(userId).orElseGet(() -> {
            Cart newCart = new Cart();
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new NotFoundException("User not found", ErrorCode.ACCOUNT_ERROR_NOT_FOUND));
            newCart.setCustomer(user);
            return cartRepository.save(newCart);
        });
    }

    public void syncAndSaveCart(Cart cart) {
        double total = cart.getCartItems().stream()
                .mapToDouble(i -> i.getPrice() * i.getQuantity())
                .sum();
        int count = cart.getCartItems().stream()
                .mapToInt(CartItem::getQuantity)
                .sum();

        cart.setTotalPrice(total);
        cart.setTotalItem(count);
        cartRepository.save(cart);
    }
}