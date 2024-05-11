package me.seho.ordersneakerssituationv3.repository.users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface GeneralUserRepository extends JpaRepository<GeneralUser, Integer> {
    List<GeneralUser> findByName(String name);
}
