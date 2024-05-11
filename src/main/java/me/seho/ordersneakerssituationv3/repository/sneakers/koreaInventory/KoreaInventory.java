package me.seho.ordersneakerssituationv3.repository.sneakers.koreaInventory;


import lombok.*;
import me.seho.ordersneakerssituationv3.repository.sneakers.sneaker.Sneaker;

import javax.persistence.*;

@Entity
@Table(name = "korea_inventory")
@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@Builder
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
