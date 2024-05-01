package com.awbd.ecommerce.service;

import com.awbd.ecommerce.dto.AddressDTO;
import com.awbd.ecommerce.dto.OrderDTO;
import com.awbd.ecommerce.dto.OrderProductDTO;
import com.awbd.ecommerce.exception.ResourceNotFoundException;
import com.awbd.ecommerce.mapper.OrderMapper;
import com.awbd.ecommerce.model.Address;
import com.awbd.ecommerce.model.Order;
import com.awbd.ecommerce.model.PaymentMethod;
import com.awbd.ecommerce.model.Product;
import com.awbd.ecommerce.model.security.User;
import com.awbd.ecommerce.repository.AddressRepository;
import com.awbd.ecommerce.repository.OrderRepository;
import com.awbd.ecommerce.repository.ProductRepository;
import com.awbd.ecommerce.repository.security.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.util.*;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private AddressRepository addressRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private OrderMapper orderMapper;
    @InjectMocks
    private OrderServiceImpl orderService;

    @Test
    void save_Success() {
        // Arrange
        Long userId = 1L;
        Long productId1 = 101L;
        Long productId2 = 102L;
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setUserId(userId);
        orderDTO.setOrderDate(LocalDate.now());
        orderDTO.setPaymentMethod(PaymentMethod.CREDIT_CARD);

        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setStreet("123 Main St");
        addressDTO.setCity("Anytown");
        addressDTO.setZipCode("12345");
        addressDTO.setCountry("USA");
        addressDTO.setDistrict("District A");
        orderDTO.setAddressDTO(addressDTO);

        List<OrderProductDTO> orderProductDTOs = new ArrayList<>();
        OrderProductDTO orderProductDTO1 = new OrderProductDTO();
        orderProductDTO1.setProductId(productId1);
        orderProductDTO1.setQuantity(2);
        OrderProductDTO orderProductDTO2 = new OrderProductDTO();
        orderProductDTO2.setProductId(productId2);
        orderProductDTO2.setQuantity(3);
        orderProductDTOs.add(orderProductDTO1);
        orderProductDTOs.add(orderProductDTO2);
        orderDTO.setOrderProductDTOS(orderProductDTOs);

        User user = new User();
        user.setId(userId);

        Product product1 = new Product();
        product1.setId(productId1);
        product1.setPrice(10.0f);
        Product product2 = new Product();
        product2.setId(productId2);
        product2.setPrice(15.0f);

        Order savedOrder = new Order();
        savedOrder.setId(1L);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(productRepository.findAllById(anyList()))
                .thenReturn(Arrays.asList(product1, product2));
        when(addressRepository.findByStreetAndCityAndZipCodeAndCountryAndDistrict(
                anyString(), anyString(), anyString(), anyString(), anyString()))
                .thenReturn(Optional.empty());
        when(addressRepository.save(any())).thenReturn(new Address());
        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder); // Return the mock savedOrder
        when(orderMapper.toDTO(any(Order.class))).thenReturn(orderDTO);

        // Act
        OrderDTO savedOrderDTO = orderService.save(orderDTO);

        // Assert
        assertNotNull(savedOrderDTO);
        assertEquals(orderDTO.getUserId(), savedOrderDTO.getUserId());
        assertEquals(orderDTO.getOrderDate(), savedOrderDTO.getOrderDate());
        assertEquals(orderDTO.getPaymentMethod(), savedOrderDTO.getPaymentMethod());
        assertEquals(orderDTO.getOrderProductDTOS().size(), savedOrderDTO.getOrderProductDTOS().size());
        verify(userRepository, times(1)).findById(userId);
        verify(productRepository, times(1)).findAllById(anyList());
        verify(addressRepository, times(1)).findByStreetAndCityAndZipCodeAndCountryAndDistrict(
                anyString(), anyString(), anyString(), anyString(), anyString());
        verify(orderRepository, times(1)).save(any(Order.class));
        verify(orderMapper, times(1)).toDTO(any(Order.class));
    }

    @Test
    void save_UserNotFound() {
        // Arrange
        long nonExistingUserId = 999L;
        when(userRepository.findById(nonExistingUserId)).thenReturn(Optional.empty());

        List<OrderProductDTO> orderProductDTOS = new ArrayList<>();

        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setUserId(nonExistingUserId);
        orderDTO.setOrderProductDTOS(orderProductDTOS);

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            orderService.save(orderDTO);
        });
    }

    @Test
    void findAll_Success() {
        // Arrange
        List<Order> orders = new ArrayList<>();
        orders.add(new Order());
        orders.add(new Order());

        List<OrderDTO> orderDTOs = new ArrayList<>();
        orderDTOs.add(new OrderDTO());
        orderDTOs.add(new OrderDTO());

        when(orderRepository.findAll()).thenReturn(orders);
        when(modelMapper.map(any(), eq(OrderDTO.class))).thenReturn(new OrderDTO());

        // Act
        List<OrderDTO> result = orderService.findAll();

        // Assert
        assertEquals(orders.size(), result.size());
        verify(orderRepository, times(1)).findAll();
        verify(modelMapper, times(orders.size())).map(any(), eq(OrderDTO.class));
    }

    @Test
    void findAllByUserId_Success() {
        // Arrange
        Long userId = 1L;
        List<Order> orders = new ArrayList<>();
        orders.add(new Order());
        orders.add(new Order());

        List<OrderDTO> orderDTOs = new ArrayList<>();
        orderDTOs.add(new OrderDTO());
        orderDTOs.add(new OrderDTO());

        when(userRepository.existsById(userId)).thenReturn(true);
        when(orderRepository.findAllByUserId(userId)).thenReturn(orders);
        when(modelMapper.map(any(), eq(OrderDTO.class))).thenReturn(new OrderDTO());

        // Act
        List<OrderDTO> result = orderService.findAllByUserId(userId);

        // Assert
        assertEquals(orders.size(), result.size());
        verify(userRepository, times(1)).existsById(userId);
        verify(orderRepository, times(1)).findAllByUserId(userId);
        verify(modelMapper, times(orders.size())).map(any(), eq(OrderDTO.class));
    }

    @Test
    void findById_Success() {
        // Arrange
        Long orderId = 1L;
        Order order = new Order();
        order.setId(orderId);

        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(orderId);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(modelMapper.map(order, OrderDTO.class)).thenReturn(orderDTO);

        // Act
        OrderDTO result = orderService.findById(orderId);

        // Assert
        assertNotNull(result);
        assertEquals(orderId, result.getId());
        verify(orderRepository, times(1)).findById(orderId);
        verify(modelMapper, times(1)).map(order, OrderDTO.class);
    }

    @Test
    void findById_OrderNotFound() {
        // Arrange
        Long nonExistingId = 999L;
        when(orderRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> orderService.findById(nonExistingId));
        verify(orderRepository, times(1)).findById(nonExistingId);
    }

    @Test
    void update_Success() {
        // Arrange
        Long orderId = 1L;
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setEmail("test@gmail.com");
        Order existingOrder = new Order();
        existingOrder.setId(orderId);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(existingOrder));

        when(orderRepository.save(existingOrder)).thenReturn(existingOrder);

        when(modelMapper.map(existingOrder, OrderDTO.class)).thenReturn(orderDTO);

        // Act
        OrderDTO updatedOrderDTO = orderService.update(orderId, orderDTO);

        // Assert
        assertNotNull(updatedOrderDTO);
        assertEquals(orderDTO.getEmail(), updatedOrderDTO.getEmail());
        verify(orderRepository).findById(orderId);
        verify(orderRepository).save(existingOrder);
        verify(modelMapper).map(existingOrder, OrderDTO.class);
    }

    @Test
    void update_OrderNotFound() {
        // Arrange
        Long orderId = 1L;
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setEmail("test@gmail.com");
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> orderService.update(orderId, orderDTO));
    }

    @Test
    void findAddressByOrderId_Success() {
        // Arrange
        Long orderId = 1L;
        AddressDTO expectedAddressDTO = new AddressDTO();
        when(orderRepository.findAddressByOrderId(orderId)).thenReturn(expectedAddressDTO);

        // Act
        AddressDTO result = orderService.findAddressByOrderId(orderId);

        // Assert
        assertEquals(expectedAddressDTO, result);
    }
}
