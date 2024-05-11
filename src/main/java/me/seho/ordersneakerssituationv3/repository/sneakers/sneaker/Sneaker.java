package me.seho.ordersneakerssituationv3.repository.sneakers.sneaker;

import lombok.*;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.persistence.*;

@Entity
@Table(name = "sneakers")
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Sneaker {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "model_id")
    private Integer modelId;

    @Column(name = "model_name")
    private String name;

    @Column(columnDefinition = "double CHECK(price >= 0.0)")
    private Double price;
}
