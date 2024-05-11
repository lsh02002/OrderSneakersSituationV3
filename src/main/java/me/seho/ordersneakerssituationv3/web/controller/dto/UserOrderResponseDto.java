package me.seho.ordersneakerssituationv3.web.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.seho.ordersneakerssituationv3.repository.Orders.Orders;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Builder
public class UserOrderResponseDto {
    private Integer orderId;
    private Integer modelId;
    private Integer sneakerSize;
    private Integer orderQuantity;
    private Double totalPrice;
    private Integer orderStatus;
    private LocalDateTime orderAt;

    public static Page<UserOrderResponseDto> toDtoList(Page<Orders> orderList){
        return orderList.map(m->UserOrderResponseDto.builder()
                .orderId(m.getOrderId())
                .modelId(m.getSneaker().getModelId())
                .sneakerSize(m.getSneakerSize())
                .orderQuantity(m.getOrderQuantity())
                .totalPrice(m.getTotalPrice())
                .orderStatus(m.getOrderStatus())
                .orderAt(m.getOrderAt())
                .build());
    }
}
