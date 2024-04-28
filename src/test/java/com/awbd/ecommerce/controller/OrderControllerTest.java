package com.awbd.ecommerce.controller;

import com.awbd.ecommerce.dto.OrderDTO;
import com.awbd.ecommerce.dto.ProductDTO;
import com.awbd.ecommerce.model.Address;
import com.awbd.ecommerce.service.OrderService;
import com.awbd.ecommerce.service.ProductService;
import com.awbd.ecommerce.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Profile;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
@AutoConfigureMockMvc
@Profile("mysql")
@ContextConfiguration(classes = {TestConfiguration.class})
public class OrderControllerTest {

    MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @MockBean
    OrderService orderService;

    @MockBean
    ProductService productService;

    @MockBean
    UserService userService;

    @MockBean
    Model model;

    @Test
    public void findAll() throws Exception {
        // arrange
        OrderDTO order1 = new OrderDTO(); order1.setOrderDate(LocalDate.now());
        OrderDTO order2 = new OrderDTO(); order2.setOrderDate(LocalDate.now());

        List<OrderDTO> orders = Arrays.asList(order1, order2);

        // act
        when(orderService.findAll()).thenReturn(orders);

        // assert
        mockMvc.perform(get("/orders"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("orders"))
                .andExpect(model().attribute("orders", orders))
                .andExpect(view().name("order-list"));

        verify(orderService, times(1)).findAll();
    }

    @Test
    public void findById_ReturnsOrderPage() throws Exception {
        Long orderId = 1L;
        OrderDTO mockOrder = new OrderDTO();
        when(orderService.findById(orderId)).thenReturn(mockOrder);

        mockMvc.perform(get("/orders/{id}", orderId))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("order"))
                .andExpect(model().attribute("order", mockOrder))
                .andExpect(view().name("order-page"));
    }

    @Test
    public void deleteById_RedirectsToOrders() throws Exception {
        Long orderId = 1L;

        mockMvc.perform(get("/orders/delete/{id}", orderId))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/orders"));

        verify(orderService).deleteById(orderId);
    }

    @Test
    public void addToCart_AddsProductAndRedirects() throws Exception {
        Long productId = 1L;
        MockHttpSession session = new MockHttpSession();

        mockMvc.perform(post("/cart/add").param("productId", productId.toString()).session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/products"));

        assertNotNull(session.getAttribute("cart"));
    }

    @Test
    @WithMockUser(username = "guest", password = "12345", roles = "GUEST")
    public void showCart_DisplaysCart() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("cart", new HashMap<Long, Integer>());

        when(productService.findById(anyLong())).thenReturn(new ProductDTO());

        mockMvc.perform(get("/cart").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("cart"));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    public void placeOrder_CreatesOrderAndRedirects() throws Exception {
        MockHttpSession session = new MockHttpSession();

        HashMap<Long, Integer> cartContents = new HashMap<>();
        cartContents.put(1L, 2);
        session.setAttribute("cart", cartContents);

        when(productService.findById(anyLong())).thenReturn(new ProductDTO());

        mockMvc.perform(post("/placeOrder")
                        .session(session)
                        .param("model", String.valueOf(model))
                        .param("paymentMethod", "CREDIT_CARD")
                        .flashAttr("addressForm", new Address()))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/products"));

        verify(orderService).save(any(OrderDTO.class));
        assertNull(session.getAttribute("cart"));
    }
}
