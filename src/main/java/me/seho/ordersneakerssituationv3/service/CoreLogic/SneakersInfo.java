package me.seho.ordersneakerssituationv3.service.CoreLogic;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.Arrays;

@Getter
@AllArgsConstructor
@ToString
public class SneakersInfo {
    private String modelName;
    private long nikeSneakersPrice;
    private String[] features;
}
