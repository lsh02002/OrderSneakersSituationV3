package me.seho.ordersneakerssituationv3.repository.Orders;

import me.seho.ordersneakerssituationv3.repository.sneakers.sneaker.Sneaker;
import me.seho.ordersneakerssituationv3.repository.users.GeneralUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Orders, Integer> {
    List<Orders> findBySneaker(Sneaker sneaker);
    Page<Orders> findByGeneralUser(GeneralUser generalUser, Pageable pageable);
}
