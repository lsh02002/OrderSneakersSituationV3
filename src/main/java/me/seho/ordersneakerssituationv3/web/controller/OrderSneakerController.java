package me.seho.ordersneakerssituationv3.web.controller;

import lombok.AllArgsConstructor;
import me.seho.ordersneakerssituationv3.service.OrderSneakerService;
import me.seho.ordersneakerssituationv3.web.controller.dto.SneakerSizeStockResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/api")
@AllArgsConstructor
public class OrderSneakerController {

    OrderSneakerService orderSneakerService;

    @GetMapping("/user-items/sneakers")
    public Page<?> getAllSneakerTraits(Pageable pageable){
        return orderSneakerService.getAllSneakerTraits(pageable);
    }

    @GetMapping("/user-items/sneakers/{sneakerId}")
    public SneakerSizeStockResponseDto getSneakerTraitById(@PathVariable Integer sneakerId, Pageable pageable){
        return orderSneakerService.getSneakerTraitById(sneakerId, pageable);
    }
}
