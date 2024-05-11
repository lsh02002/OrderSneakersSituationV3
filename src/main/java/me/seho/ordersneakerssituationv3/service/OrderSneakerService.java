package me.seho.ordersneakerssituationv3.service;

import lombok.RequiredArgsConstructor;
import me.seho.ordersneakerssituationv3.repository.sneakers.abroadInventory.AbroadInventory;
import me.seho.ordersneakerssituationv3.repository.sneakers.abroadInventory.AbroadInventoryRepository;
import me.seho.ordersneakerssituationv3.repository.sneakers.koreaInventory.KoreaInventory;
import me.seho.ordersneakerssituationv3.repository.sneakers.koreaInventory.KoreaInventoryRepository;
import me.seho.ordersneakerssituationv3.repository.sneakers.sneaker.*;
import me.seho.ordersneakerssituationv3.web.controller.dto.InventoryDto;
import me.seho.ordersneakerssituationv3.web.controller.dto.SneakerModelTraitResponseDto;
import me.seho.ordersneakerssituationv3.web.controller.dto.SneakerSizeStockResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderSneakerService {

    private final SneakerRepository sneakerRepository;
    private final SneakerModelTraitRepository sneakerModelTraitRepository;
    private final SneakerTraitRepostiory sneakerTraitRepostiory;
    private final AbroadInventoryRepository abroadInventoryRepository;
    private final KoreaInventoryRepository koreaInventoryRepository;

    @Transactional(readOnly = true)
    public Page<?> getAllSneakerTraits(Pageable pageable) {
        List<SneakerModelTrait> sneakerModelTraits = sneakerModelTraitRepository.findAll();
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
    public SneakerSizeStockResponseDto getSneakerTraitById(Integer sneakerId, Pageable pageable) {
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
}