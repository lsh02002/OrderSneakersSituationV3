package me.seho.ordersneakerssituationv3.repository.users;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "general_users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class GeneralUser {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name ="g_user_id")
    private Integer gUserId;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String name;

    @Column(name = "favorite_shopping_address", length = 50)
    private String favoriteShoppingAddress;

    @Column(name = "my_card_num", length = 30)
    private String myCardNum;

    @Column(name ="my_bank_account")
    private String myBankAccount;
}
