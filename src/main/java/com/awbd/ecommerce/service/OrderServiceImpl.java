package com.awbd.ecommerce.service;

import com.awbd.ecommerce.dto.OrderDTO;
import com.awbd.ecommerce.dto.OrderProductDTO;
import com.awbd.ecommerce.exception.ResourceNotFoundException;
import com.awbd.ecommerce.helper.BeanHelper;
import com.awbd.ecommerce.mapper.OrderMapper;
import com.awbd.ecommerce.model.*;
import com.awbd.ecommerce.repository.*;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
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

    @Override
    @Transactional
    public OrderDTO save(OrderDTO orderDTO) {
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
                throw new ResourceNotFoundException("Product with id " + orderProductDTO.getProductId() + " not found!");
            }
        }

        User user = userRepository.findById(orderDTO.getUserId())
                .orElseThrow(()->new ResourceNotFoundException("User with id " + orderDTO.getUserId() + " not found!"));

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

        order.setUser(user);
        order.setOrderProducts(orderProducts);
        order.setOrderDate(orderDTO.getOrderDate());
        order.setPaymentMethod(orderDTO.getPaymentMethod());
        order.setTotalAmount(totalAmount);

        Order savedOrder = orderRepository.save(order);
        return orderMapper.toDTO(savedOrder);
    }

    @Override
    public List<OrderDTO> findAll() {
        List<Order> orders = orderRepository.findAll();

        return orders.stream()
                .map(order -> modelMapper.map(order, OrderDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public OrderDTO findById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Order with id " + id + " not found!"));

        return modelMapper.map(order, OrderDTO.class);
    }

    @Override
    public void deleteById(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new ResourceNotFoundException("Order with id " + id + " not found!");
        }

        orderRepository.deleteById(id);
    }

    @Override
    public OrderDTO update(Long id, OrderDTO orderDTO) {
        Order order = orderRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Order with id"  + id + " not found!"));

        BeanUtils.copyProperties(orderDTO, order, BeanHelper.getNullPropertyNames(orderDTO));

        orderRepository.save(order);

        return modelMapper.map(order, OrderDTO.class);
    }
}
