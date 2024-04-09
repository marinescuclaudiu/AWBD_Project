package com.awbd.ecommerce.mapper;

import com.awbd.ecommerce.dto.AddressDTO;
import com.awbd.ecommerce.dto.OrderDTO;
import com.awbd.ecommerce.dto.OrderProductDTO;
import com.awbd.ecommerce.model.Order;
import com.awbd.ecommerce.model.OrderProduct;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrderMapper {
    ModelMapper modelMapper;

    public OrderMapper(ModelMapper modelMapper){
        this.modelMapper = modelMapper;
    }
    public OrderDTO toDTO(Order order){

        AddressDTO addressDTO = modelMapper.map(order.getAddress(), AddressDTO.class);

        List<OrderProductDTO> orderProductDTOS = new ArrayList<>();
        for(OrderProduct orderProduct : order.getOrderProducts()) {
            orderProductDTOS.add(new OrderProductDTO(
                    orderProduct.getId(),
                    orderProduct.getProduct().getName(),
                    orderProduct.getQuantity()
            ));
        }

        return new OrderDTO(
            order.getId(),
            order.getUser().getId(),
            order.getUser().getEmail(),
            addressDTO,
            orderProductDTOS,
            order.getTotalAmount(),
            order.getPaymentMethod()
        );
    }
}
