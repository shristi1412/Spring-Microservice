package com.program.techie.OrderService.service;

import com.program.techie.OrderService.dto.InventoryResponse;
import com.program.techie.OrderService.dto.OrderLineItemsDto;
import com.program.techie.OrderService.dto.OrderRequest;
import com.program.techie.OrderService.model.Order;
import com.program.techie.OrderService.model.OrderLineItems;
import com.program.techie.OrderService.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    private final WebClient.Builder webClientBuilder;

    public void placeOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderLineItems> list = orderRequest.getOrderLineItemsDtoList().stream().map(this::maptoDto).toList();

        order.setOrderLineItemsList(list);

        List<String> skuCodes = order.getOrderLineItemsList().stream().map(OrderLineItems::getSkuCode).toList();

        //call inventory service and place order if its available in inventory
        InventoryResponse[] inventoryResponse = webClientBuilder.build().get().uri("http://InventoryService/api/inventory",
                        uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
                .retrieve().
                bodyToMono(InventoryResponse[].class).block();


        boolean isInstock = Arrays.stream(inventoryResponse).allMatch(InventoryResponse::isInStock);
        if (isInstock) {
            orderRepository.save(order);
        } else {
            throw new IllegalArgumentException("Could not find oreder, please try again later");
        }
    }

    private OrderLineItems maptoDto(OrderLineItemsDto orderLineItemsDto) {
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
        orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
        orderLineItems.setPrice(orderLineItemsDto.getPrice());
        return orderLineItems;
    }
}
