package me.seho.ordersneakerssituationv3.service;

import lombok.RequiredArgsConstructor;
import me.seho.ordersneakerssituationv3.repository.Orders.*;
import me.seho.ordersneakerssituationv3.repository.sneakers.abroadInventory.AbroadInventory;
import me.seho.ordersneakerssituationv3.repository.sneakers.abroadInventory.AbroadInventoryRepository;
import me.seho.ordersneakerssituationv3.repository.sneakers.koreaInventory.KoreaInventory;
import me.seho.ordersneakerssituationv3.repository.sneakers.koreaInventory.KoreaInventoryRepository;
import me.seho.ordersneakerssituationv3.repository.sneakers.sneaker.*;
import me.seho.ordersneakerssituationv3.repository.users.GeneralUser;
import me.seho.ordersneakerssituationv3.repository.users.GeneralUserRepository;
import me.seho.ordersneakerssituationv3.web.controller.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderSneakerService {

    private final GeneralUserRepository generalUserRepository;
    private final SneakerRepository sneakerRepository;
    private final SneakerModelTraitRepository sneakerModelTraitRepository;
    private final SneakerTraitRepostiory sneakerTraitRepostiory;
    private final AbroadInventoryRepository abroadInventoryRepository;
    private final KoreaInventoryRepository koreaInventoryRepository;
    private final OrderRepository orderRepository;
    private final WishRepository wishRepository;
    private final PaymentRepository paymentRepository;

    @Transactional(readOnly = true)
    public Page<?> getAllSneakerTraits(Pageable pageable) {
        Page<SneakerModelTrait> sneakerModelTraits = sneakerModelTraitRepository.findAll(pageable);
        List<SneakerModelTraitResponseDto> sneakerModelTraitResponseDtos = new ArrayList<>();

        for (SneakerModelTrait sneakerModelTrait : sneakerModelTraits) {
            Integer id = sneakerModelTrait.getModelTraitId();
            SneakerTrait sneakerTrait = sneakerTraitRepostiory.findById(id).get();

            String[] features = sneakerTrait.getDescriptions() != null ? sneakerTrait.getDescriptions().split(",") : sneakerTrait.getEnglishDesc().split(",");

            SneakerModelTraitResponseDto responseDto = SneakerModelTraitResponseDto.builder()
                    .modelId(id)
                    .modelTraits(features)
                    .build();

            sneakerModelTraitResponseDtos.add(responseDto);
        }

        PageRequest pageRequest = PageRequest.ofSize(pageable.getPageSize());
        int start = (int) pageRequest.getOffset();
        int end = Math.min((start + pageRequest.getPageSize()), sneakerModelTraitResponseDtos.size());

        return new PageImpl<>(sneakerModelTraitResponseDtos.subList(start, end), pageRequest, sneakerModelTraitResponseDtos.size());
    }

    @Transactional(readOnly = true)
    public SneakerSizeStockResponseDto getSneakerTraitById(Integer sneakerId) {
        Sneaker sneaker = sneakerRepository.findById(sneakerId).get();
        SneakerModelTrait sneakerModelTrait = sneakerModelTraitRepository.findBySneaker(sneaker);
        SneakerTrait sneakerTrait = sneakerTraitRepostiory.findByTraitId(sneakerModelTrait.getModelTraitId());

        List<AbroadInventory> abroadInventories = abroadInventoryRepository.findBySneaker(sneaker);
        List<KoreaInventory> koreaInventories = koreaInventoryRepository.findBySneaker(sneaker);

        List<InventoryDto> inventoryDtos = new ArrayList<>();

        String[] features = sneakerTrait.getDescriptions() != null ? sneakerTrait.getDescriptions().split(",") : sneakerTrait.getEnglishDesc().split(",");

        if (!abroadInventories.isEmpty()) {
            for(AbroadInventory abroadInventory : abroadInventories){
                InventoryDto inventoryDto = InventoryDto.builder()
                        .sneakerSize(abroadInventory.getSneakerSize())
                        .stock(abroadInventory.getStock())
                        .build();

                inventoryDtos.add(inventoryDto);
            }

        } else if (!koreaInventories.isEmpty() ) {
            for(KoreaInventory koreaInventory : koreaInventories){
                InventoryDto inventoryDto = InventoryDto.builder()
                        .sneakerSize(koreaInventory.getSneakerSize())
                        .stock(koreaInventory.getStock())
                        .build();

                inventoryDtos.add(inventoryDto);
            }

        } else {
            throw new RuntimeException("제품 목록에 없습니다.");
        }

        return SneakerSizeStockResponseDto.builder()
                .modelId(sneakerId)
                .modelTraits(features)
                .price(sneaker.getPrice().intValue())
                .SneakerInventories(inventoryDtos)
                .build();
    }

    @Transactional
    public Double setUserOrder(UserOrderRequestDto requestDto){
        Sneaker sneaker = sneakerRepository.findById(requestDto.getModel_id()).get();
        GeneralUser generalUser = generalUserRepository.findById(requestDto.getUser_id()).get();
        String shippingAddress;

        if(requestDto.getShipping_address() == null){
            shippingAddress = generalUser.getFavoriteShoppingAddress();
        } else if(generalUser.getFavoriteShoppingAddress() != null ){
            shippingAddress = requestDto.getShipping_address();
        } else {
            throw new RuntimeException("주소를 찾을 수 없습니다");
        }

        Orders order = Orders.builder()
                .generalUser(generalUser)
                .sneaker(sneaker)
                .shippingAddress(shippingAddress)
                .sneakerSize(requestDto.getSneaker_size())
                .orderAt(LocalDateTime.now())
                .orderQuantity(requestDto.getOrder_quantity())
                .orderStatus(1)
                .build();

        orderRepository.save(order);

        return sneaker.getPrice();
    }

    @Transactional
    public Boolean setUserWish(UserWishRequestDto requestDto){
        try {
            Sneaker sneaker = sneakerRepository.findById(requestDto.getModel_id()).get();
            GeneralUser generalUser = generalUserRepository.findById(requestDto.getUser_id()).get();

            Wish wish = Wish.builder()
                    .sneaker(sneaker)
                    .generalUser(generalUser)
                    .sneakerSize(requestDto.getSneaker_size())
                    .expectReplenismentDate(null)
                    .wishAt(LocalDateTime.now())
                    .build();

            wishRepository.save(wish);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    @Transactional(readOnly = true)
    public Page<UserOrderResponseDto> orderPay(Integer generalUserId, Pageable pageable){
        GeneralUser generalUser = generalUserRepository.findById(generalUserId).get();
        Page<Orders> orders = orderRepository.findByGeneralUser(generalUser, pageable);

        return UserOrderResponseDto.toDtoList(orders);
    }

    @Transactional
    public Boolean makePayment(UserPayRequestDto requestDto) {
        try {
            GeneralUser generalUser = generalUserRepository.findById(requestDto.getGeneral_user_id()).get();
            Orders order = orderRepository.findById(requestDto.getOrder_id()).get();

            Integer cardType = null;

            if (requestDto.getType().equals("계좌 이체")) {
                cardType = 2;
            }
            Payment payment = Payment.builder()
                    .paymentAt(LocalDateTime.now())
                    .type(cardType)
                    .generalUser(generalUser)
                    .sneaker(order.getSneaker())
                    .build();

            paymentRepository.save(payment);
            return true;
        }catch (Exception e){
            return false;
        }
    }
}