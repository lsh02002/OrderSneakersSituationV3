package me.seho.ordersneakerssituationv3.repository.sneakers.abroadInventory;

import me.seho.ordersneakerssituationv3.repository.sneakers.sneaker.Sneaker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface AbroadInventoryRepository extends JpaRepository<AbroadInventory, Integer> {
    List<AbroadInventory> findBySneaker(Sneaker sneaker);
}
