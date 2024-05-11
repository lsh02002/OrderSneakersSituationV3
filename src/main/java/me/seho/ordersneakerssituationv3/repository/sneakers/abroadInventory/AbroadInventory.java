package me.seho.ordersneakerssituationv3.repository.sneakers;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "abroad_inventory")
@Getter
@Setter
public class AbroadInventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "abroad_inventory_id")
    private Integer abroadInventoryId;

    @OneToOne
    @JoinColumn(name = "model_id")
    private Sneaker sneaker;

    @Column(name = "sneaker_size", columnDefinition = "integer CHECK(sneaker_size >= 100)")
    private Integer sneakerSize;

    @Column(columnDefinition = "integer CHECK(stock >= 0)")
    private Integer stock;

    @Column(name ="expect_delivered_day")
    private Integer expectedDeliveredDay;
}
