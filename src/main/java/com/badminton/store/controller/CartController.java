package com.badminton.store.controller;

import com.badminton.store.dto.ApiMessageDto;
import com.badminton.store.dto.ErrorCode;
import com.badminton.store.dto.cart.CartDto;
import com.badminton.store.form.cart.AddToCartForm;
import com.badminton.store.form.cart.UpdateCartForm; // Cần tạo thêm form này
import com.badminton.store.mapper.CartMapper;
import com.badminton.store.model.*;
import com.badminton.store.repository.*;
import com.badminton.store.exception.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Iterator;
import java.util.Optional;

@RestController
@RequestMapping("/v1/cart")
@Api(tags = "Cart Controller", description = "Quản lý giỏ hàng người dùng")
public class CartController extends ABasicController {

    @Autowired private CartRepository cartRepository;
    @Autowired private CartItemRepository cartItemRepository;
    @Autowired private ProductRepository productRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private CartMapper cartMapper;

    @ApiOperation(value = "Thêm/Cộng dồn sản phẩm vào giỏ hàng")
    @PostMapping(value = "/add", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ApiMessageDto<Void> addToCart(@Valid @RequestBody AddToCartForm form) {
        Long userId = getCurrentUser();

        // 1. Lấy hoặc tạo giỏ hàng
        Cart cart = getOrCreateCart(userId);

        // 2. Kiểm tra sản phẩm và tồn kho
        Product product = productRepository.findById(form.getProductId())
                .orElseThrow(() -> new NotFoundException("Sản phẩm không tồn tại", ErrorCode.PRODUCT_ERROR_NOT_FOUND));

        if (product.getStatus() != 1) { // Giả sử 1 là ACTIVE
            throw new BadRequestException("Sản phẩm hiện không còn kinh doanh", ErrorCode.PRODUCT_ERROR_NOT_FOUND);
        }

        // 3. Tìm xem sản phẩm đã có trong giỏ chưa
        CartItem existingItem = cart.getCartItems().stream()
                .filter(i -> i.getProduct().getId().equals(product.getId()))
                .findFirst()
                .orElse(null);

        int newQuantity = (existingItem != null) ? existingItem.getQuantity() + form.getQuantity() : form.getQuantity();

        // Kiểm tra tồn kho thực tế
        if (product.getQuantity() < newQuantity) {
            throw new BadRequestException("Số lượng trong kho không đủ (Còn: " + product.getQuantity() + ")", ErrorCode.PRODUCT_ERROR_OUT_OF_STOCK);
        }

        if (existingItem != null) {
            existingItem.setQuantity(newQuantity);
            existingItem.setPrice(product.getPrice()); // Cập nhật giá mới nhất
        } else {
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setProduct(product);
            newItem.setQuantity(form.getQuantity());
            newItem.setPrice(product.getPrice());
            cart.getCartItems().add(newItem);
        }

        // 4. Đồng bộ tổng tiền và lưu
        syncAndSaveCart(cart);

        return makeSuccessResponse("Đã thêm vào giỏ hàng thành công.");
    }

    @ApiOperation(value = "Cập nhật số lượng cụ thể (Dùng trong trang Cart)")
    @PutMapping(value = "/update-quantity", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ApiMessageDto<Void> updateQuantity(@Valid @RequestBody UpdateCartForm form) {
        Cart cart = cartRepository.findById(getCurrentUser())
                .orElseThrow(() -> new NotFoundException("Giỏ hàng không tồn tại", ErrorCode.PRODUCT_ERROR_NOT_FOUND));

        CartItem item = cart.getCartItems().stream()
                .filter(i -> i.getProduct().getId().equals(form.getProductId()))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Sản phẩm không có trong giỏ", ErrorCode.PRODUCT_ERROR_NOT_FOUND));

        if (form.getQuantity() <= 0) {
            cart.getCartItems().remove(item);
        } else {
            // Kiểm tra tồn kho trước khi cập nhật số lượng mới
            if (item.getProduct().getQuantity() < form.getQuantity()) {
                throw new BadRequestException("Kho không đủ hàng", ErrorCode.PRODUCT_ERROR_OUT_OF_STOCK);
            }
            item.setQuantity(form.getQuantity());
            item.setPrice(item.getProduct().getPrice()); // Cập nhật giá mới nhất
        }

        syncAndSaveCart(cart);
        return makeSuccessResponse("Cập nhật số lượng thành công.");
    }

    @ApiOperation(value = "Lấy giỏ hàng và đồng bộ giá mới nhất")
    @GetMapping(value = "/my-cart", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional // Dùng transactional vì có thể sẽ update lại giá trong DB khi sync
    public ApiMessageDto<CartDto> getMyCart() {
        Cart cart = getOrCreateCart(getCurrentUser());

        // Logic thực tế: Trước khi trả về, phải kiểm tra xem sản phẩm có bị ẩn/đổi giá không
        boolean isChanged = false;
        Iterator<CartItem> iterator = cart.getCartItems().iterator();
        while (iterator.hasNext()) {
            CartItem item = iterator.next();
            Product p = item.getProduct();

            if (p == null || p.getStatus() != 1) { // Sản phẩm bị xóa hoặc ngừng bán
                iterator.remove();
                isChanged = true;
            } else if (!item.getPrice().equals(p.getPrice())) { // Giá sản phẩm thay đổi
                item.setPrice(p.getPrice());
                isChanged = true;
            }
        }

        if (isChanged) {
            syncAndSaveCart(cart);
        }

        return makeSuccessResponse(cartMapper.fromEntityToDto(cart), "Lấy giỏ hàng thành công.");
    }

    @ApiOperation(value = "Xóa một sản phẩm khỏi giỏ")
    @DeleteMapping(value = "/remove-item/{productId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ApiMessageDto<Void> removeItem(@PathVariable Long productId) {
        Cart cart = cartRepository.findById(getCurrentUser())
                .orElseThrow(() -> new NotFoundException("Cart không tồn tại"));

        boolean removed = cart.getCartItems().removeIf(i -> i.getProduct().getId().equals(productId));

        if (removed) {
            syncAndSaveCart(cart);
        }

        return makeSuccessResponse("Đã xóa sản phẩm khỏi giỏ.");
    }

    @ApiOperation(value = "Làm trống giỏ hàng")
    @DeleteMapping(value = "/clear", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ApiMessageDto<Void> clearCart() {
        Cart cart = cartRepository.findById(getCurrentUser()).orElse(null);
        if (cart != null) {
            cart.getCartItems().clear();
            cart.setTotalPrice(0.0);
            cart.setTotalItem(0);
            cartRepository.save(cart);
        }
        return makeSuccessResponse("Giỏ hàng đã được làm trống.");
    }

    // --- CÁC HÀM TRỢ GIÚP (PRIVATE METHODS) ---

    private Cart getOrCreateCart(Long userId) {
        return cartRepository.findById(userId).orElseGet(() -> {
            Cart newCart = new Cart();
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new NotFoundException("User not found", ErrorCode.ACCOUNT_ERROR_NOT_FOUND));
            newCart.setCustomer(user);
            return cartRepository.save(newCart);
        });
    }

    private void syncAndSaveCart(Cart cart) {
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