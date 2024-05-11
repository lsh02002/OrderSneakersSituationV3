package me.seho.ordersneakerssituationv3.web.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class UserWishRequestDto {
    private Integer model_id;
    private Integer user_id;
    private Integer sneaker_size;
}
