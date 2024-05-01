package com.awbd.ecommerce.service;

import com.awbd.ecommerce.dto.AddressDTO;
import com.awbd.ecommerce.dto.OrderDTO;
import com.awbd.ecommerce.dto.OrderProductDTO;
import com.awbd.ecommerce.exception.ResourceNotFoundException;
import com.awbd.ecommerce.helper.BeanHelper;
import com.awbd.ecommerce.mapper.OrderMapper;
import com.awbd.ecommerce.model.*;
import com.awbd.ecommerce.model.security.User;
import com.awbd.ecommerce.repository.*;
import com.awbd.ecommerce.repository.security.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService{
        OrderRepository orderRepository;
        OrderProductRepository orderProductRepository;
        ProductRepository productRepository;
        AddressRepository addressRepository;
        UserRepository userRepository;
        ModelMapper modelMapper;
        OrderMapper orderMapper;

    public OrderServiceImpl(OrderRepository orderRepository, OrderProductRepository orderProductRepository,
                            ProductRepository productRepository, AddressRepository addressRepository,
                            UserRepository userRepository, ModelMapper modelMapper, OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.orderProductRepository = orderProductRepository;
        this.productRepository = productRepository;
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.orderMapper = orderMapper;
    }

    @Transactional
    @Override
    public OrderDTO save(OrderDTO orderDTO) {
        log.info("Saving new order");
        Order order = new Order();

        List<OrderProductDTO> orderProductDTOS = orderDTO.getOrderProductDTOS();

        List<Long> productIds = orderProductDTOS.stream()
                .map(OrderProductDTO::getProductId)
                .collect(Collectors.toList());

        Map<Long, Product> productMap = productRepository.findAllById(productIds).stream()
                .collect(Collectors.toMap(Product::getId, p -> p));

        List<OrderProduct> orderProducts = new ArrayList<>();
        float totalAmount = 0;

        for (OrderProductDTO orderProductDTO : orderProductDTOS) {

            Product product = productMap.get(orderProductDTO.getProductId());

            if (product != null) {
                orderProducts.add(new OrderProduct(orderProductDTO.getQuantity(), product.getPrice() * orderProductDTO.getQuantity(), product, order));
                totalAmount += product.getPrice() * orderProductDTO.getQuantity();
            } else {
                log.error("Product with id {} not found!", orderProductDTO.getProductId());
                throw new ResourceNotFoundException("Product with id " + orderProductDTO.getProductId() + " not found!");
            }
        }

        Optional<User> user = userRepository.findById(orderDTO.getUserId());

        if(user.isEmpty()){
            log.error("Order with id {} not found!", orderDTO.getUserId());
            throw new ResourceNotFoundException("User with id " + orderDTO.getUserId() + " not found!");
        }

        Optional<Address> existingAddress = addressRepository.findByStreetAndCityAndZipCodeAndCountryAndDistrict(
                orderDTO.getAddressDTO().getStreet(),
                orderDTO.getAddressDTO().getCity(),
                orderDTO.getAddressDTO().getZipCode(),
                orderDTO.getAddressDTO().getCountry(),
                orderDTO.getAddressDTO().getDistrict()
        );

        if(existingAddress.isEmpty()){
           Address address = addressRepository.save(modelMapper.map(orderDTO.getAddressDTO(), Address.class));
           order.setAddress(address);
        } else {
            order.setAddress(existingAddress.get());
        }

        order.setUser(user.get());
        order.setOrderProducts(orderProducts);
        order.setOrderDate(orderDTO.getOrderDate());
        order.setPaymentMethod(orderDTO.getPaymentMethod());
        order.setTotalAmount(totalAmount);

        Order savedOrder = orderRepository.save(order);

        log.info("Order with id {} saved", savedOrder.getId());
        return orderMapper.toDTO(savedOrder);
    }

    @Override
    public List<OrderDTO> findAll() {
        log.info("Fetching all orders");
        List<Order> orders = orderRepository.findAll();

        log.info("Found {} orders", orders.size());
        return orders.stream()
                .map(order -> modelMapper.map(order, OrderDTO.class))
                .collect(Collectors.toList());
    }

    public List<OrderDTO> findAllByUserId(Long userId){
        log.info("Fetching all orders for user with id: {}", userId);

        if(!userRepository.existsById(userId)){
            log.error("User with id {} not found!", userId);
            throw new ResourceNotFoundException("User with id " + userId + " not found!");
        }

        List<Order> orders = orderRepository.findAllByUserId(userId);

        log.info("Found {} orders", orders.size());
        return orders.stream()
                .map(order -> modelMapper.map(order, OrderDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public OrderDTO findById(Long id) {
        log.info("Fetching order by ID: {}", id);
        Optional<Order> order = orderRepository.findById(id);

        if(order.isEmpty()){
            log.error("Order with id {} not found!", id);
            throw new ResourceNotFoundException("Order with id " + id + " not found!");
        }

        log.info("Order found: {}", order.get().getId());
        return modelMapper.map(order.get(), OrderDTO.class);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        log.info("Deleting order by ID: {}", id);

        if (!orderRepository.existsById(id)) {
            log.error("Order with id {} not found", id);
            throw new ResourceNotFoundException("Order with id " + id + " not found!");
        }

        orderRepository.deleteById(id);
        log.info("Order deleted successfully");
    }

    @Transactional
    @Override
    public OrderDTO update(Long id, OrderDTO orderDTO) {
        log.info("Updating category with ID: {}", id);
        Optional<Order> order = orderRepository.findById(id);

        if(order.isEmpty()){
            log.error("Order with id {} not found", id);
            throw new  ResourceNotFoundException("Order with id"  + id + " not found!");
        }
        BeanUtils.copyProperties(orderDTO, order.get(), BeanHelper.getNullPropertyNames(orderDTO));

        orderRepository.save(order.get());

        log.info("Order updated: {}", order.get().getId());
        return modelMapper.map(order.get(), OrderDTO.class);
    }

    @Override
    public AddressDTO findAddressByOrderId(Long orderId) {
        return orderRepository.findAddressByOrderId(orderId);
    }
}
