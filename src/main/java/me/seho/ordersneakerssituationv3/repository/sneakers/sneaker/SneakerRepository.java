package me.seho.ordersneakerssituationv3.repository.sneakers.sneaker;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface SneakerRepository extends JpaRepository<Sneaker, Integer> {
    Sneaker findByName(String name);
}
