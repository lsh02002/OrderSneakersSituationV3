package me.seho.ordersneakerssituationv3.repository.sneakers;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;

@Entity
@Table(name = "korea_inventory")
@Getter
@Setter
public class KoreaInventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "korea_inventory_id")
    private Integer koreaInventoryId;

    @OneToOne
    @JoinColumn(name = "model_id")
    private Sneaker sneaker;

    @Column(name = "sneaker_size", columnDefinition = "integer CHECK(sneaker_size >= 100)")
    private Integer sneakerSize;

    @Column(columnDefinition = "integer CHECK(stock >= 0)")
    private Integer stock;
}
