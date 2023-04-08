//package service;
//
//
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//import com.fmss.orderservice.repository.OrderRepository;
//import jakarta.transaction.Transactional;
//import jakarta.validation.constraints.NotNull;
//import lombok.RequiredArgsConstructor;
//import lombok.SneakyThrows;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//
//import java.math.BigDecimal;
//import java.util.*;
//import java.util.function.Function;
//import java.util.stream.Collectors;
//
//@RequiredArgsConstructor
//@Slf4j
//@Service
//public class OrderServiceImpl implements OrderService {
//
//    private final OrderRepository orderRepository;
//
//    private final OrderMapper orderMapper;
//
//    private final OrderItemMapper orderItemMapper;
//
//    private final ObjectMapper objectMapper;
//    private final ProducerService producerService;
//    private final OrderOutBoxService orderOutBoxService;
//    private final ProductFeignClient productFeignClient;
//    private final CacheClient cacheClient;
//    private final PaymentSystem paymentSystem;
//    private final CardMapper cardMapper;
//    private final OrderItemRepository orderItemRepository;
//    private final CardRepository cardRepository;
//
//    @Override
//    @Transactional
//    public OrderResponseDTO placeOrder(@NotNull PlaceOrderRequestDTO placeOrderRequestDTO) {
//        validateProductPrice(placeOrderRequestDTO.getBranchId(), placeOrderRequestDTO.getOrderItems());
//        validateProductStatus(placeOrderRequestDTO.getOrderItems());
//
//        var order = orderMapper.convertOrderFromPlaceOrderRequestDTO(placeOrderRequestDTO);
//
//        order.setOrderStatus(OrderStatus.RECEIVED);
//
//        Order orderCreated = orderRepository.saveAndFlush(order);
//
//        var orderItemList = placeOrderRequestDTO.getOrderItems().stream()
//                .map(orderItemMapper::convertOrderItemFromOrderItemRequestDTO).toList();
//
//        orderItemList.forEach(orderItem -> orderItem.setOrderId(orderCreated.getOrderId()));
//        List<OrderItem> orderItems = orderItemRepository.saveAll(orderItemList);
//
//        cardRepository.save(cardMapper.convertCardFromCardInfoDto(placeOrderRequestDTO.getCardInfo()));
//
//        PaymentResponse paymentResponse = paymentSystem.pay(placeOrderRequestDTO.getOrderItems(),
//                cardMapper.convertCardFromCardInfoDto(placeOrderRequestDTO.getCardInfo()));
//
//
//        if (Objects.isNull(paymentResponse) || !paymentResponse.getPaymentStatus().equals(PaymentStatus.SUCCESS)) {
//            throw new PaymentFailureException("Payment failure");
//        }
//        producerService.sendMessage(orderCreated);
//
//        OrderOutbox orderOutbox;
//        try {
//            orderOutbox = OrderOutbox.builder()
//                    .orderPayload(objectMapper.writeValueAsString(order))
//                    .paymentId(String.valueOf(paymentResponse.getId()))
//                    .orderId(orderCreated.getOrderId())
//                    .build();
//            orderOutBoxService.saveOrderOutbox(orderOutbox);
//        } catch (Exception e) {
//            // TODO:Buradaki orderoutbox kayitlari baska bir db;ye atilabilir diye dusundum
//            log.error("Error order outbox {}", e.getMessage());
//        }
//
//
//        return orderMapper.convertPlaceOrderResponseDTOFromOrder(orderCreated, orderItems);
//    }
//
//
//    public void validateProductPrice(UUID branchId, List<OrderItemRequestDTO> orderItems) {
//        ProductPricesRequestDto productPricesRequestDto = ProductPricesRequestDto.builder()
//                .productIds(orderItems.stream().map(OrderItemRequestDTO::getProductId).toList())
//                .build();
//
//        List<ProductPriceResponseDto> productPrices = productFeignClient.getProductPrices(branchId, productPricesRequestDto);
//
//        Map<UUID, ProductPriceResponseDto> productIdPriceMap = productPrices.stream()
//                .collect(Collectors.toMap(ProductPriceResponseDto::getProductId, Function.identity()));
//
//
//        for (OrderItemRequestDTO orderItem : orderItems) {
//            ProductPriceResponseDto productPrice = productIdPriceMap.get(orderItem.getProductId());
//
//            if (Objects.isNull(productPrice)) {
//                throw new ProductPriceNotFoundException();
//            }
//            if (!orderItem.getUnitPrice().equals(productPrice.getPrice())) {
//                throw new InConsistentProductPriceException("Product price not match");
//            }
//
//            if (!orderItem.getTotalPrice().equals(productPrice.getPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity())))) {
//                throw new InConsistentProductPriceException("Total price not match");
//            }
//
//
//        }
//
//    }
//
//    public void validateProductStatus(List<OrderItemRequestDTO> orderItems) {
//        List<ProductStatusCacheDto> cacheStatus = orderItems.stream()
//                .map(item -> getProductStatusFromCache(item.getProductId().toString())).toList();
//        for (ProductStatusCacheDto status : cacheStatus) {
//            if (status.getProductStatus().equals(ProductStatus.PASSIVE.toString()))
//                throw new ProductPriceNotFoundException("Products status passive");
//        }
//
//
//    }
//
//
//    @SneakyThrows
//    public ProductStatusCacheDto getProductStatusFromCache(String productId) {
//        Object o = cacheClient.get(productId);
//        JsonNode jsonNode = objectMapper.readTree(o.toString());
//        ProductStatus productStatus = ProductStatus.valueOf(jsonNode.get("productStatus").asText());
//
//        return ProductStatusCacheDto.builder()
//                .productStatus(productStatus.toString())
//                .productId(UUID.fromString(productId).toString())
//                .build();
//    }
