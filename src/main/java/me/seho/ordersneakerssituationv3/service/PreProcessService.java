package me.seho.ordersneakerssituationv3.service;

import lombok.RequiredArgsConstructor;
import me.seho.ordersneakerssituationv3.repository.Orders.OrderRepository;
import me.seho.ordersneakerssituationv3.repository.Orders.Orders;
import me.seho.ordersneakerssituationv3.repository.Orders.Payment;
import me.seho.ordersneakerssituationv3.repository.Orders.PaymentRepository;
import me.seho.ordersneakerssituationv3.repository.sneakers.abroadInventory.AbroadInventory;
import me.seho.ordersneakerssituationv3.repository.sneakers.abroadInventory.AbroadInventoryRepository;
import me.seho.ordersneakerssituationv3.repository.sneakers.koreaInventory.KoreaInventory;
import me.seho.ordersneakerssituationv3.repository.sneakers.koreaInventory.KoreaInventoryRepository;
import me.seho.ordersneakerssituationv3.repository.sneakers.sneaker.*;
import me.seho.ordersneakerssituationv3.repository.users.GeneralUser;
import me.seho.ordersneakerssituationv3.repository.users.GeneralUserRepository;
import me.seho.ordersneakerssituationv3.repository.users.User;
import me.seho.ordersneakerssituationv3.repository.users.UserRepository;
import me.seho.ordersneakerssituationv3.service.CoreLogic.*;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.time.LocalTime.now;

@Service
@RequiredArgsConstructor
public class PreProcessService {

    //이전 과제에서 처럼 영업 내용을 DB에 넣는 서비스입니다.

    private final UserRepository userRepository;
    private final GeneralUserRepository generalUserRepository;
    private final SneakerRepository sneakerRepository;
    private final AbroadInventoryRepository abroadInventoryRepository;
    private final KoreaInventoryRepository koreaInventoryRepository;
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final SneakerTraitRepostiory sneakerTraitRepostiory;
    private final SneakerModelTraitRepository sneakerModelTraitRepository;

    List<Customer> customers = new ArrayList<>();

    final long TODAY_START_SALES_AMOUNT = 0;
    Staff staff = new Staff(TODAY_START_SALES_AMOUNT);

    public void PreProcess(){

        readFileAndSetSneakerInfoMap();
        readFileAndSetSneakersStockMap();

        System.out.println("직원: 운동화 재고 정보 다음과 같이 숙지하였습니다." + this.staff.getSneakersStockMap());

        inputCustomerData();

        System.out.println("고객 대기 리스트 등록 완료되었습니다.\n" + customers);

        // 영업 모듈입니다.
        try{
            for(Customer customer : customers) {
                boolean havingNikeSneakersInStore = staff.checkHavingNikeSneakersInStore();
                staff.setHavingNikeSneakersInStore(havingNikeSneakersInStore);

                DeliveryManager deliveryManager = new DeliveryManager();
                deliveryManager.setSalesAmount(100_000);

                SneakersInfo nikeSneakerInfo = staff.getSneakersInfoMap().get(customer.getSneakerModel());
                deliveryManager.setNikeSneakerInfo(nikeSneakerInfo);
                //deliveryManager.setPackageInfo(sneakerPackageInfo);

                // 여기서 로직
                customer.askNikeSneakersToStaff(staff);
                long nikePrice = customer.askAndGetSneakerPriceFromStaff(staff, customer.getSneakerModel());

                if (!customer.isAffordable(nikePrice)) {
                    customer.sayBye();
                    continue;
                }

                if (staff.checkHavingNikeSneakersInStore()) {
                    customer.sayOrder();
                    staff.sayPayment(nikePrice);
                    long cache = customer.makePayment(nikePrice);

                    // ***** 주문 처리 모듈입니다. *****
                    makePayment(customer);

                    staff.addSalesAmount(cache, customer);

                    staff.offerNikeSneakers();
                    customer.sayAboutCustomer();
                    continue;
                }

                if (!customer.isLikeDelivery()) {
                    customer.sayBye();
                    continue;
                }

                customer.sayOrder();
                staff.sayPayment(nikePrice);
                long cache = customer.makePayment(nikePrice);

                staff.addSalesAmount(cache, customer);

                SneakerPackageInfo nikeSneakerPackageInfo = staff.orderNikeSneakersToDeliverManager(deliveryManager);
                staff.sayNikePackageInfo(nikeSneakerPackageInfo);
                long deliverCost = staff.calculateDeliveryCost(nikeSneakerPackageInfo, customer);

                if (customer.isAffordable(deliverCost)) {
                    customer.requireRefund();
                    long refundCache = staff.returnRefund(cache);
                    customer.getRefund(refundCache);
                    customer.sayBye();
                    continue;
                }
                customer.sayOrder();
                SneakerPackage sneakerPackage = deliveryManager.makeSneakerPackage();
                long deliveryCost = customer.askAndGetDeliverCostFromDeliveryManager(deliveryManager);
                deliveryManager.sayPayment(deliveryCost);

                long cachePackage = customer.makePayment(deliveryCost);
                deliveryManager.addSalesAmount(cachePackage);

                customer.wearSneakers(sneakerPackage.beUnBoxed());
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public boolean isAlphanumerics(final String codePoints) {

        for(int i=0; i<codePoints.length(); i++) {
            int codePoint = codePoints.charAt(i);

            if (!((codePoint >= 65 && codePoint <= 90) ||
                    (codePoint >= 97 && codePoint <= 122) ||
                    (codePoint >= 48 && codePoint <= 57))){
                return false;
            }
        }
        return true;
    }
    public void readFileAndSetSneakerInfoMap() {
        // 스니커즈 모델 입력 모듈입니다.

        try(BufferedReader fis = new BufferedReader(new FileReader("src/main/resources/nike-sneaker-characters.txt"))) {

            String line;
            while (true) {
                line = fis.readLine();
                if (line == null) break;

                String[] strArray = line.split("\\|");
                String modelName = strArray[0];
                long price = Long.parseLong(strArray[1]);
                String[] features = strArray[2].split(",");

                SneakersInfo sneakersInfo = new SneakersInfo(modelName, price, features);
                staff.getSneakersInfoMap().put(modelName, sneakersInfo);

                Sneaker sneaker = Sneaker.builder()
                        .name(modelName)
                        .price((double) price)
                        .build();

                sneakerRepository.save(sneaker);

                SneakerTrait sneakerTrait = null;

                if (isAlphanumerics(features[0])) {
                    AbroadInventory abroadInventory = AbroadInventory.builder()
                            .sneakerSize(170)
                            .sneaker(sneaker)
                            .build();

                    abroadInventoryRepository.save(abroadInventory);

                    sneakerTrait = SneakerTrait.builder()
                            .englishDesc(strArray[2])
                            .build();

                    sneakerTraitRepostiory.save(sneakerTrait);
                } else {
                    KoreaInventory koreaInventory = KoreaInventory.builder()
                            .sneakerSize(170)
                            .sneaker(sneaker)
                            .build();

                    koreaInventoryRepository.save(koreaInventory);

                    sneakerTrait = SneakerTrait.builder()
                            .descriptions(strArray[2])
                            .build();

                    sneakerTraitRepostiory.save(sneakerTrait);
                }

                SneakerModelTrait sneakerModelTrait = SneakerModelTrait.builder()
                        .sneaker(sneaker)
                        .sneakerTrait(sneakerTrait)
                        .build();

                sneakerModelTraitRepository.save(sneakerModelTrait);
            }
        } catch (IOException | RuntimeException e) {
            e.printStackTrace();
        }
    }

    public void readFileAndSetSneakersStockMap(){
        // 스니커지 재고 입력 모듈입니다.
        try(BufferedReader fis = new BufferedReader(new FileReader("src/main/resources/nick-sneaker-stocks.txt"))){

            String line;
            while(true){
                line = fis.readLine();
                if(line == null) break;

                String[] strArray = line.split("\\|");
                String modelName = strArray[0];
                Integer stock = Integer.parseInt(strArray[1]);

                staff.getSneakersStockMap().put(modelName, stock.longValue());

                Sneaker sneaker = sneakerRepository.findByName(modelName);

                if (sneaker == null) {
                    throw new RuntimeException("해당 모델에 대한 검색 결과가 없습니다");
                }

                List<AbroadInventory> abroadInventories = abroadInventoryRepository.findBySneaker(sneaker);
                List<KoreaInventory> koreaInventories = koreaInventoryRepository.findBySneaker(sneaker);

                if (!abroadInventories.isEmpty()) {
                    abroadInventories.get(0).setStock(stock);
                    abroadInventoryRepository.save(abroadInventories.get(0));

                } else if (!koreaInventories.isEmpty()) {
                    koreaInventories.get(0).setStock(stock);
                    koreaInventoryRepository.save(koreaInventories.get(0));

                } else {
                    throw new RuntimeException("제품 목록에 없습니다.");
                }
            }
        } catch (IOException | RuntimeException e) {
            e.printStackTrace();
        }
    }

    public void inputCustomerData(){
        // 고객 입력 처리 모듈입니다.
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

                if (customerLevel.equals(CustomerLevel.GENERAL)
                        || customerLevel.equals(CustomerLevel.SILVER)
                        || customerLevel.equals(CustomerLevel.GOLD)
                        || customerLevel.equals(CustomerLevel.VIP)) {
                    GeneralUser generalUser = GeneralUser.builder()
                            .user(user)
                            .name(customerName)
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

                customers.add(customer);
                ;

                i++;
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void makePayment(Customer customer){

        //DB에 동일 이름이 있는 것 같습니다.
        GeneralUser generalUser = generalUserRepository.findByName(customer.getCustomerName()).get(0);
        Sneaker sneaker = sneakerRepository.findByName(customer.getSneakerModel());

        Integer sneakerSize = 0;
        Double sneakerPrice = 0.0;

        List<AbroadInventory> abroadInventories = abroadInventoryRepository.findBySneaker(sneaker);
        List<KoreaInventory> koreaInventories = koreaInventoryRepository.findBySneaker(sneaker);

        if (!abroadInventories.isEmpty()) {
            sneakerSize = abroadInventories.get(0).getSneakerSize();
            sneakerPrice = sneaker.getPrice();
        } else if (!koreaInventories.isEmpty()) {
            sneakerSize = koreaInventories.get(0).getSneakerSize();
            sneakerPrice = sneaker.getPrice();

        } else {
            throw new RuntimeException("제품 목록에 없습니다.");
        }

        Orders order = Orders.builder()
                .generalUser(generalUser)
                .sneaker(sneaker)
                .orderStatus(1)
                .shippingAddress(generalUser.getFavoriteShoppingAddress())
                .sneakerSize(sneakerSize)
                .totalPrice(sneakerPrice)
                .orderAt(LocalDateTime.now())
                .build();

        orderRepository.save(order);

        Payment payment = Payment.builder()
                .paymentAt(LocalDateTime.now())
                .type(1)
                .generalUser(generalUser)
                .sneaker(sneaker)
                .build();

        paymentRepository.save(payment);
    }
}
