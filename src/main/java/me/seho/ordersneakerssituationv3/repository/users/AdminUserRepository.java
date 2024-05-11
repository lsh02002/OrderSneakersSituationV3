package me.seho.ordersneakerssituationv3.repository.users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface AdminUserRepository extends JpaRepository<AdminUser, Integer> {
}
