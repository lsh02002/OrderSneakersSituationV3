package me.seho.ordersneakerssituationv3.web.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class UserPayRequestDto {
    private Integer general_user_id;
    private Integer order_id;
    private String type;
}
