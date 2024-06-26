package me.seho.ordersneakerssituationv3.repository.sneakers.sneaker;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "sneaker_model_traits")
@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@Builder
public class SneakerModelTrait {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "model_trait_id")
    private Integer modelTraitId;

    @OneToOne
    @JoinColumn(name = "model_id", nullable = false)
    private Sneaker sneaker;

    @OneToOne
    @JoinColumn(name = "trait_id", nullable = false)
    private SneakerTrait sneakerTrait;
}
