package me.seho.ordersneakerssituationv3.repository.Orders;


import lombok.Getter;
import lombok.Setter;
import me.seho.ordersneakerssituationv3.repository.sneakers.sneaker.Sneaker;
import me.seho.ordersneakerssituationv3.repository.users.GeneralUser;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "wishs")
@Getter
@Setter
public class Wish {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wish_id")
    private Integer wishId;

    @ManyToOne
    @JoinColumn(name = "g_user_id", nullable = false)
    private GeneralUser generalUser;

    @ManyToOne
    @JoinColumn(name = "model_id", nullable = false)
    private Sneaker sneaker;

    @Column(name = "expect_replenisment_date")
    private LocalDateTime expectReplenismentDate;

    @Column(name = "wish_at", nullable = false)
    private LocalDateTime wishAt;
}
