package me.seho.ordersneakerssituationv3.service;

import lombok.RequiredArgsConstructor;
import me.seho.ordersneakerssituationv3.repository.sneakers.abroadInventory.AbroadInventory;
import me.seho.ordersneakerssituationv3.repository.sneakers.abroadInventory.AbroadInventoryRepository;
import me.seho.ordersneakerssituationv3.repository.sneakers.koreaInventory.KoreaInventory;
import me.seho.ordersneakerssituationv3.repository.sneakers.koreaInventory.KoreaInventoryRepository;
import me.seho.ordersneakerssituationv3.repository.sneakers.sneaker.Sneaker;
import me.seho.ordersneakerssituationv3.repository.sneakers.sneaker.SneakerRepository;
import me.seho.ordersneakerssituationv3.repository.users.GeneralUser;
import me.seho.ordersneakerssituationv3.repository.users.GeneralUserRepository;
import me.seho.ordersneakerssituationv3.repository.users.User;
import me.seho.ordersneakerssituationv3.repository.users.UserRepository;
import me.seho.ordersneakerssituationv3.service.CoreLogic.Customer;
import me.seho.ordersneakerssituationv3.service.CoreLogic.Staff;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.time.LocalTime.now;

@Service
@RequiredArgsConstructor
public class ProcessService {

    private final UserRepository userRepository;
    private final GeneralUserRepository generalUserRepository;
    private final SneakerRepository sneakerRepository;
    private final AbroadInventoryRepository abroadInventoryRepository;
    private final KoreaInventoryRepository koreaInventoryRepository;

    List<Customer> customers = new ArrayList<>();

    final long TODAY_START_SALES_AMOUNT = 0;
    Staff staff = new Staff(TODAY_START_SALES_AMOUNT);

    public void InsertDataToDBServiceMethod() {

        staff.readFileAndSetSneakerInfoMap();
        staff.readFileAndSetSneakersStockMap();
    }

    public void Process(){

        InsertDataToDBServiceMethod();

        try (BufferedReader fis = new BufferedReader(new FileReader("src/main/resources/customer-inputs.txt"))) {
            int i = 0;

            while (true) {
                String response = fis.readLine();
                String[] responseArray = response.split(",");

                if (response.equals("끝")) {
                    break;
                }

                CustomerLevel customerLevel = CustomerLevel.valueOf(responseArray[0]);
                String customerName = responseArray[1];

                boolean isCustomerLikeDelivery = Boolean.parseBoolean(responseArray[2]);
                Long cache = Long.parseLong(responseArray[3]);
                String sneakerModel = responseArray[4];

                /* 고객 대기 목록 명단에 고객 객체 넣어야합니다.*/

                User user = User.builder()
                        .name(customerName)
                        .email("email" + i + "@gmail.com")
                        .phone_num("01000000000")
                        .build();

                userRepository.save(user);

                if(customerLevel.equals(CustomerLevel.GENERAL)
                        || customerLevel.equals(CustomerLevel.SILVER)
                        || customerLevel.equals(CustomerLevel.GOLD)
                        || customerLevel.equals(CustomerLevel.VIP)){
                    GeneralUser generalUser = GeneralUser.builder()
                            .user(user)
                            .favoriteShoppingAddress("서울시 종로구")
                            .myBankAccount("000-000-000")
                            .myCardNum("1234")
                            .build();

                    generalUserRepository.save(generalUser);
                }

                Customer customer = Customer.builder()
                        .customerLevel(customerLevel)
                        .customerName(customerName)
                        .likeDelivery(isCustomerLikeDelivery)
                        .cache(cache)
                        .sneakerModel(sneakerModel)
                        .build();

                customers.add(customer);;

                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        
    }
}
