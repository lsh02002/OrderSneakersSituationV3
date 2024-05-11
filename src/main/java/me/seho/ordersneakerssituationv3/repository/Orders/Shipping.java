package me.seho.ordersneakerssituationv3.repository.Orders;

import lombok.Getter;
import lombok.Setter;
import me.seho.ordersneakerssituationv3.repository.sneakers.sneaker.Sneaker;
import me.seho.ordersneakerssituationv3.repository.users.AdminUser;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "shipping")
@Getter
@Setter
public class Shipping {
    @Id
    @Column(name = "shipping_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer shippingId;

    @ManyToOne
    @JoinColumn(name = "admin_id")
    private AdminUser adminUser;

    @ManyToOne
    @JoinColumn(name = "model_id")
    private Sneaker sneaker;

    @OneToOne
    @JoinColumn(name = "order_id")
    private Orders orders;

    @Column(name = "shipping_address", length = 30, nullable = false)
    private String shippingAddress;

    @Column(name = "shipping_at", nullable = false)
    private LocalDateTime shippingAt;
}
