package com.awbd.ecommerce.controller;

import com.awbd.ecommerce.dto.OrderDTO;
import com.awbd.ecommerce.dto.ProductDTO;
import com.awbd.ecommerce.dto.UserDTO;
import com.awbd.ecommerce.model.Address;
import com.awbd.ecommerce.service.OrderService;
import com.awbd.ecommerce.service.ProductService;
import com.awbd.ecommerce.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.Model;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("mysql")
public class OrderControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    OrderService orderService;

    @MockBean
    ProductService productService;

    @MockBean
    UserService userService;

    @MockBean
    Model model;

    @Test
    @WithMockUser(username = "guest", password = "12345", roles = "GUEST")
    public void findAll() throws Exception {
        OrderDTO order1 = new OrderDTO();
        order1.setOrderDate(LocalDate.now());
        OrderDTO order2 = new OrderDTO();
        order2.setOrderDate(LocalDate.now());

        List<OrderDTO> orders = Arrays.asList(order1, order2);

        when(orderService.findAll()).thenReturn(orders);

        mockMvc.perform(get("/orders"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("orders"))
                .andExpect(model().attribute("orders", orders))
                .andExpect(view().name("order-list"));

        verify(orderService, times(1)).findAll();
    }

    @Test
    @WithMockUser(username = "guest", password = "12345", roles = "GUEST")
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
    @WithMockUser(username = "guest", password = "12345", roles = "GUEST")
    public void deleteById_RedirectsToOrders() throws Exception {
        Long orderId = 1L;

        mockMvc.perform(get("/orders/delete/{id}", orderId))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/orders"));

        verify(orderService).deleteById(orderId);
    }

    @Test
    @WithMockUser(username = "guest", password = "12345", roles = "GUEST")
    public void testAddToCart_emptyCard() throws Exception {
        Long productId = 1L; // Example product ID
        MockHttpSession session = new MockHttpSession();

        mockMvc.perform(post("/cart/add")
                        .session(session)
                        .param("productId", productId.toString())
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/products"));

        Map<Long, Integer> cart = (Map<Long, Integer>) session.getAttribute("cart");
        assertNotNull(cart, "Cart should not be null");
        assertEquals(1, cart.size(), "Cart should have one product");
        assertTrue(cart.containsKey(productId), "Cart should contain the product ID");
        assertEquals(1, (int) cart.get(productId), "Product quantity should be 1");
    }

    @Test
    @WithMockUser(username = "guest", password = "12345", roles = "GUEST")
    public void testAddToCart_ExistingProductsInCart() throws Exception {
        Long productId = 1L;
        MockHttpSession session = new MockHttpSession();
        Map<Long, Integer> existingCart = new HashMap<>();
        existingCart.put(productId, 1);
        session.setAttribute("cart", existingCart);

        mockMvc.perform(post("/cart/add")
                        .session(session)
                        .param("productId", productId.toString())
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/products"));

        Map<Long, Integer> cart = (Map<Long, Integer>) session.getAttribute("cart");
        assertNotNull(cart, "Cart should not be null");
        assertEquals(1, cart.size(), "Cart should have one product");
        assertEquals(2, (int) cart.get(productId), "Product quantity should be to 2");
    }

    @Test
    @WithMockUser(username = "guest", password = "12345", roles = "GUEST")
    public void showCart() throws Exception {
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

        Address theAddress = new Address();
        theAddress.setStreet("Str. Unirii");
        theAddress.setCity("Bucharest");
        theAddress.setZipCode("123456");
        theAddress.setCountry("Romania");
        theAddress.setDistrict("Sectorul 1");

        UserDTO mockUserDTO = new UserDTO();
        mockUserDTO.setId(1L);
        when(userService.findByUsername("user")).thenReturn(mockUserDTO);

        when(productService.findById(anyLong())).thenReturn(new ProductDTO());

        mockMvc.perform(post("/placeOrder")
                        .session(session)
                        .param("model", String.valueOf(model))
                        .param("paymentMethod", "CREDIT_CARD")
                        .flashAttr("addressForm", theAddress)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/products"));

        verify(orderService).save(any(OrderDTO.class));
        assertNull(session.getAttribute("cart"));
    }
}
