package me.seho.ordersneakerssituationv3.repository.sneakers;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "sneakers")
@Getter
@Setter
public class Sneaker {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "model_id")
    private Integer modelId;

    @Column(columnDefinition = "double CHECK(price >= 0.0)")
    private Double price;
}
