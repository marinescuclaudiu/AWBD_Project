package com.awbd.ecommerce.controller;

import com.awbd.ecommerce.dto.*;
import com.awbd.ecommerce.exception.ResourceNotFoundException;
import com.awbd.ecommerce.model.Address;
import com.awbd.ecommerce.model.PaymentMethod;
import com.awbd.ecommerce.service.OrderService;
import com.awbd.ecommerce.service.ProductService;
import com.awbd.ecommerce.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
public class OrderController {
    OrderService orderService;

    ProductService productService;

    UserService userService;
    ModelMapper modelMapper;

    public OrderController(OrderService orderService, ProductService productService, UserService userService, ModelMapper modelMapper) {
        this.orderService = orderService;
        this.productService = productService;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }


    @GetMapping("/orders/{id}")
    public String findById(@PathVariable Long id, Model model) {
        try {
            OrderDTO orderDTO = orderService.findById(id);
            model.addAttribute("order", orderDTO);
            return "order-page";
        } catch (ResourceNotFoundException exception) {
            model.addAttribute("exception", exception);
            return "notFoundException";
        }
    }

    @GetMapping("/orders")
    public String findAll(Model model) {
        List<OrderDTO> orders = orderService.findAll();
        model.addAttribute("orders", orders);

        return "order-list";
    }

    @RequestMapping("/orders/delete/{id}")
    public String deleteById(@PathVariable Long id, Model model) {
        try {
            orderService.deleteById(id);
            return "redirect:/orders";
        } catch (ResourceNotFoundException exception) {
            model.addAttribute("exception", exception);
            return "notFoundException";
        }
    }

    @PostMapping("/cart/add")
    public String addToCart(Long productId, HttpSession session) {
        Map<Long, Integer> cart = (Map<Long, Integer>) session.getAttribute("cart");
        if (cart == null) {
            cart = new HashMap<>();
        }
        cart.put(productId, cart.getOrDefault(productId, 0) + 1);
        session.setAttribute("cart", cart);

        return "redirect:/products";
    }

    @RequestMapping("/cart")
    public String showCart(Model model, HttpSession session) {
        Map<Long, Integer> cart = (Map<Long, Integer>) session.getAttribute("cart");
        if (cart == null) {
            cart = new HashMap<>();
        }

        Map<ProductDTO, Integer> productsInCart = new LinkedHashMap<>();
        for (Map.Entry<Long, Integer> entry : cart.entrySet()) {
            ProductDTO productDTO = productService.findById(entry.getKey());
            if (productDTO != null) {
                productsInCart.put(productDTO, entry.getValue());
            }
        }

        model.addAttribute("productsInCart", productsInCart);

        return "cart";
    }

    @PostMapping("/placeOrder")
    public String placeOrder(@Valid @ModelAttribute("addressForm") Address addressForm, String paymentMethod, HttpSession session, Model model) {
        Map<Long, Integer> cart = (Map<Long, Integer>) session.getAttribute("cart");

        OrderDTO orderDTO = new OrderDTO();

        if (orderDTO.getOrderProductDTOS() == null) {
            orderDTO.setOrderProductDTOS(new ArrayList<>());
        }

        for (Map.Entry<Long, Integer> order_product : cart.entrySet()) {
            OrderProductDTO orderProductDTO = new OrderProductDTO();

            orderProductDTO.setProductId(order_product.getKey());
            orderProductDTO.setQuantity(order_product.getValue());
            orderProductDTO.setName(
                    productService.findById(order_product.getKey()).getName()
            );

            orderDTO.getOrderProductDTOS().add(orderProductDTO);
        }

        if (cart.isEmpty()) {
            model.addAttribute("error", "Your cart is empty.");
            return "redirect:/cart"; // Redirect back to cart page
        }

        orderDTO.setAddressDTO(modelMapper.map(addressForm, AddressDTO.class));

        Long currentUserId = getCurrentUserId();
        orderDTO.setUserId(currentUserId);

        orderDTO.setPaymentMethod(PaymentMethod.valueOf(paymentMethod));

        orderService.save(orderDTO);

        session.removeAttribute("cart"); // Clear the cart after placing the order
        return "redirect:/products";
    }

    public Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName();
        UserDTO userDTO = userService.findByUsername(username);

        return userDTO.getId();
    }
}
