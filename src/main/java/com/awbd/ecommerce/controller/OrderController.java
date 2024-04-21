package com.awbd.ecommerce.controller;

import com.awbd.ecommerce.dto.*;
import com.awbd.ecommerce.model.Address;
import com.awbd.ecommerce.model.PaymentMethod;
import com.awbd.ecommerce.service.OrderService;
import com.awbd.ecommerce.service.ProductService;
import com.awbd.ecommerce.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

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
        OrderDTO orderDTO = orderService.findById(id);
        model.addAttribute("order", orderDTO);

        return "/orders/order-page";
    }

    // TODO: findAll, deleteById, update, button to add or decrease wuantity
//
//    @GetMapping
//    public ResponseEntity<List<OrderDTO>> findAll() {
//        List<OrderDTO> orders = orderService.findAll();
//        return ResponseEntity.ok().body(orders);
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
//        orderService.deleteById(id);
//        return ResponseEntity.noContent().build();
//    }
//
//    @PatchMapping("/{id}")
//    public ResponseEntity<OrderDTO> update(@PathVariable Long id, @RequestBody OrderDTO orderDTO){
//        return ResponseEntity.ok().body(orderService.update(id, orderDTO));
//    }

    //------------------------

//    @PostMapping("/cart/increase")
//    public String increaseQuantity(@RequestParam("productId") Long productId, HttpSession session) {
//        System.out.println("---------------------");
//        System.out.println("in function for increase");
//
//        updateQuantity(productId, 1, session);
//        return "redirect:/cart";
//    }
//
//    @PostMapping("/cart/decrease")
//    public String decreaseQuantity(@RequestParam("productId") Long productId, HttpSession session) {
//        updateQuantity(productId, -1, session);
//        return "redirect:/cart";
//    }
//
//    private void updateQuantity(Long productId, int change, HttpSession session) {
//        List<OrderProductDTO> cart = (List<OrderProductDTO>) session.getAttribute("cart");
//        if (cart == null) {
//            cart = new ArrayList<>();
//        }
//        Optional<OrderProductDTO> productInCart = cart.stream()
//                .filter(p -> productId.equals(p.getProductId()))
//                .findFirst();
//
//        System.out.println("##----product in cart to be modified##");
//        System.out.println(productInCart);
//
//        if (productInCart.isPresent()) {
//            OrderProductDTO productDTO = productInCart.get();
//            int newQuantity = productDTO.getQuantity() + change;
//
//            System.out.println("##----product in cart to be modified##");
//            System.out.println(newQuantity);
//
//            if (newQuantity > 0) {
//                productDTO.setQuantity(newQuantity);
//            } else {
//                cart.remove(productDTO);
//            }
//        } else if (change > 0) {
//            // Add new product to cart if it's not present and increment is requested
//            ProductDTO productDTO = productService.findById(productId);
//            cart.add(new OrderProductDTO(productId, productDTO.getName(), 1));
//        }
//
//        session.setAttribute("cart", cart);
//    }

    @PostMapping("/cart/add")
    public String addToCart(Long productId, HttpSession session) {
        Map<Long, Integer> cart = (Map<Long, Integer>) session.getAttribute("cart");
        if (cart == null) {
            cart = new HashMap<>();
        }
        cart.put(productId, cart.getOrDefault(productId, 0) + 1);
        session.setAttribute("cart", cart);

        System.out.println(session.getAttribute("cart"));

        return "redirect:/products";
    }

    @GetMapping("/cart")
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
        return "/orders/cart";
    }


    @PostMapping("/placeOrder")
    public String placeOrder(Address addressForm, String paymentMethod, HttpSession session, Model model) {
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
