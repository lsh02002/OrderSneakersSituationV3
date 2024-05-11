package me.seho.ordersneakerssituationv3.repository.Orders;

import lombok.*;
import me.seho.ordersneakerssituationv3.repository.sneakers.sneaker.Sneaker;
import me.seho.ordersneakerssituationv3.repository.users.GeneralUser;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "payment")
@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@Builder
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Integer paymentId;

    @ManyToOne
    @JoinColumn(name = "g_user_id", nullable = false)
    private GeneralUser generalUser;

    @ManyToOne
    @JoinColumn(name = "model_id", nullable = false)
    private Sneaker sneaker;

    @Column(nullable = false)
    private Integer type;

    @Column(name = "payment_at", nullable = false)
    private LocalDateTime paymentAt;
}
