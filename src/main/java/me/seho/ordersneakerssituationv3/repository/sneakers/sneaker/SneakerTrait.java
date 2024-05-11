package me.seho.ordersneakerssituationv3.repository.sneakers.sneaker;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "sneaker_traits")
@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@Builder
public class SneakerTrait {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "trait_id")
    private Integer traitId;

    private String descriptions;

    @Column(name = "english_desc")
    private String englishDesc;
}
