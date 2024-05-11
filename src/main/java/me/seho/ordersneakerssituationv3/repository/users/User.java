package me.seho.ordersneakerssituationv3.repository.users;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer userId;

    @Column(length = 30, nullable = false)
    private String name;

    @Column(length = 30, unique = true, nullable = false)
    private String email;

    @Column(length = 12, nullable = false)
    private String phone_num;
}
