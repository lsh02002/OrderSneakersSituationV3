package me.seho.ordersneakerssituationv3.repository.Orders;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {
}
