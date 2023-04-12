package com.fmss.orderservice.mapper;


import com.fmss.orderservice.dto.PlaceOrderRequestDTO;
import com.fmss.orderservice.model.Order;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public abstract class OrderMapper {

    public abstract Order convertOrderFromPlaceOrderRequestDTO(PlaceOrderRequestDTO orderRequestDto);



}
