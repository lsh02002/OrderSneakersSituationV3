package me.seho.ordersneakerssituationv3.repository.Orders;

import lombok.*;
import me.seho.ordersneakerssituationv3.repository.sneakers.sneaker.Sneaker;
import me.seho.ordersneakerssituationv3.repository.users.GeneralUser;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@Builder
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Integer orderId;

    @ManyToOne
    @JoinColumn(name = "g_user_id", nullable = false)
    private GeneralUser generalUser;

    @ManyToOne
    @JoinColumn(name = "model_id", nullable = false)
    private Sneaker sneaker;

    @Column(name = "sneaker_size", nullable = false)
    private Integer sneakerSize;

    @Column(name = "shipping_address", length = 30, nullable = false)
    private String shippingAddress;

    @Column(name = "total_price")
    private Double totalPrice;

    @Column(name = "order_status")
    private Integer orderStatus;

    @Column(name = "order_at", nullable = false)
    private LocalDateTime orderAt;

}
