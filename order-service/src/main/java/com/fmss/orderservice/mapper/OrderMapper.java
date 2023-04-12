package com.fmss.orderservice.mapper;



import com.fmss.commondata.dtos.response.OrderResponseDTO;
import com.fmss.orderservice.dto.PlaceOrderRequestDTO;
import com.fmss.orderservice.model.Order;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface OrderMapper {

    Order convertOrderFromPlaceOrderRequestDTO(PlaceOrderRequestDTO orderRequestDto);

    OrderResponseDTO convertOrderFResponseDtoFromOrder(Order order);


}
