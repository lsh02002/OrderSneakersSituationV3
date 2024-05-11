package me.seho.ordersneakerssituationv3.repository.sneakers;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "sneaker_traits")
@Getter
@Setter
public class SneakerTrait {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "trait_id")
    private Integer traitId;

    private String descriptions;

    @Column(name = "english_desc")
    private String englishDesc;
}
