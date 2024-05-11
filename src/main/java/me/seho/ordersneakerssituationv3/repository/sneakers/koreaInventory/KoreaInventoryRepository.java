package me.seho.ordersneakerssituationv3.repository.sneakers.koreaInventory;

import me.seho.ordersneakerssituationv3.repository.sneakers.abroadInventory.AbroadInventory;
import me.seho.ordersneakerssituationv3.repository.sneakers.sneaker.Sneaker;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KoreaInventoryRepository extends JpaRepository<KoreaInventory, Integer> {
    List<KoreaInventory> findBySneaker(Sneaker sneaker);
}
