package me.seho.ordersneakerssituationv3.web.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class UserOrderRequestDto {
    private Integer model_id;
    private Integer user_id;
    private String shipping_address;
    private Integer sneaker_size;
    private Integer order_quantity;
}
