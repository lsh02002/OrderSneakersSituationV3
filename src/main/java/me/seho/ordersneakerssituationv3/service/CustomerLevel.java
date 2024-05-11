package me.seho.ordersneakerssituationv3.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import javax.persistence.Table;

@Getter
@AllArgsConstructor
@ToString
public enum CustomerLevel {
    VIP("vip 등급 고객"),
    GOLD("gold 등급 고객"),
    SILVER("silver 등급 고객"),
    GENERAL("일반 등급 고객");

    private final String koreanName;
}
