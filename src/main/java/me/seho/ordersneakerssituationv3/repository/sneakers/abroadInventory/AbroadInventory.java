package me.seho.ordersneakerssituationv3.repository.sneakers.abroadInventory;

import lombok.*;
import me.seho.ordersneakerssituationv3.repository.sneakers.sneaker.Sneaker;

import javax.persistence.*;

@Entity
@Table(name = "abroad_inventory")
@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@Builder
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
