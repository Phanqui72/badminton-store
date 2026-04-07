package com.badminton.store.controller;

import com.badminton.store.dto.ApiMessageDto;
import com.badminton.store.dto.ResponseListDto;
import com.badminton.store.dto.order.OrderDto;
import com.badminton.store.exception.BadRequestException;
import com.badminton.store.exception.NotFoundException;
import com.badminton.store.form.order.CreateOrderForm;
import com.badminton.store.mapper.OrderMapper;
import com.badminton.store.model.*;
import com.badminton.store.repository.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.badminton.store.constant.MgrConstant.*;
import static com.badminton.store.dto.ErrorCode.*;

@RestController
@RequestMapping("/v1/order")
@Api(tags = "Order Controller")
public class OrderController extends ABasicController {

    @Autowired private OrderRepository orderRepository;
    @Autowired private CartRepository cartRepository;
    @Autowired private ProductRepository productRepository;
    @Autowired private CouponRepository couponRepository;
    @Autowired private OrderMapper orderMapper;

    @ApiOperation(value = "Checkout order")
    @PostMapping(value = "/checkout", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ApiMessageDto<String> checkout(@Valid @RequestBody CreateOrderForm form, BindingResult bindingResult) {
        Long userId = getCurrentUser();

        Cart cart = cartRepository.findById(userId)
                .orElseThrow(() -> new BadRequestException("Cart not found", "ERROR-CART-0001"));

        if (cart.getCartItems() == null || cart.getCartItems().isEmpty()) {
            throw new BadRequestException("Cart is empty", "ERROR-CART-0002");
        }

        Coupon coupon = null;
        if (StringUtils.isNotBlank(form.getCouponCode())) {
            coupon = couponRepository.findFirstByCodeAndStatus(form.getCouponCode(), STATUS_ACTIVE)
                    .orElseThrow(() -> new NotFoundException("Coupon invalid", COUPON_ERROR_NOT_FOUND));

            if (coupon.getExpiredDate().isBefore(LocalDateTime.now())) {
                throw new BadRequestException("Coupon expired", COUPON_ERROR_EXPIRED);
            }
            if (coupon.getUsed() >= coupon.getLimitUsage()) {
                throw new BadRequestException("Coupon out of stock", COUPON_ERROR_OUT_OF_STOCK);
            }
        }

        Order order = new Order();
        order.setCustomer(cart.getCustomer());
        order.setShippingAddress(form.getShippingAddress());
        order.setReceiverName(form.getReceiverName());
        order.setReceiverPhone(form.getReceiverPhone());
        order.setStatus(ORDER_STATUS_PENDING);
        order.setCoupon(coupon);

        double subTotal = 0;
        List<OrderDetail> details = new ArrayList<>();
        for (CartItem item : cart.getCartItems()) {
            Product product = item.getProduct();
            if (product.getQuantity() < item.getQuantity()) {
                throw new BadRequestException("Product " + product.getTitle() + " out of stock", "ERROR-PRODUCT-0002");
            }

            product.setQuantity(product.getQuantity() - item.getQuantity());
            productRepository.save(product);

            OrderDetail detail = new OrderDetail();
            detail.setOrder(order);
            detail.setProduct(product);
            detail.setQuantity(item.getQuantity());
            detail.setPrice(product.getPrice());
            details.add(detail);
            subTotal += detail.getPrice() * detail.getQuantity();
        }

        if (coupon != null) {
            double discount = (coupon.getDiscountType() == COUPON_TYPE_FIXED) ?
                    coupon.getDiscountValue() : (subTotal * coupon.getDiscountValue() / 100);
            subTotal = Math.max(0, subTotal - discount);
            coupon.setUsed(coupon.getUsed() + 1);
            couponRepository.save(coupon);
        }

        order.setTotalPrice(subTotal);
        order.setOrderDetails(details);
        orderRepository.save(order);

        // Xóa giỏ hàng
        cart.getCartItems().clear();
        cart.setTotalPrice(TOTAL_ORDER_PRICE_DEFAULT);
        cart.setTotalItem(TOTAL_ITEM_DEFAULT);
        cartRepository.save(cart);

        return makeSuccessResponse("Order created successful.");
    }

    @ApiOperation(value = "Lấy danh sách đơn hàng")
    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListDto<List<OrderDto>>> list(Pageable pageable) {
        Page<Order> page = orderRepository.findAll(pageable);
        return makeSuccessResponse(makeResponseListDto(page, list -> orderMapper.fromEntityListToDtoList(list)), "List success");
    }
}