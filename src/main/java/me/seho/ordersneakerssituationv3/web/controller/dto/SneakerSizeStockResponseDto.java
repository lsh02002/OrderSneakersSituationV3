package me.seho.ordersneakerssituationv3.web.controller.dto;


import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SneakerSizeStockResponseDto {
    private Integer modelId;
    private String[] modelTraits;
    private Integer price;
    private List<InventoryDto> SneakerInventories;


}
