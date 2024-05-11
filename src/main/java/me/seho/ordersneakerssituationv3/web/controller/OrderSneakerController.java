package me.seho.ordersneakerssituationv3.web.controller;

import lombok.AllArgsConstructor;
import me.seho.ordersneakerssituationv3.service.OrderSneakerService;
import me.seho.ordersneakerssituationv3.web.controller.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.function.BinaryOperator;

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
    public SneakerSizeStockResponseDto getSneakerTraitById(@PathVariable Integer sneakerId){
        return orderSneakerService.getSneakerTraitById(sneakerId);
    }

    @PostMapping("/user-order-wish/order")
    public ResponseEntity<String> setUserOrder(@RequestBody UserOrderRequestDto requestDto){
        Double totalPrice = orderSneakerService.setUserOrder(requestDto);

        if(totalPrice > 0){
            return ResponseEntity.ok("구매하신 물품 " + totalPrice + "이고 예약 완료되었습니다.");
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("찜한 물품이 예약 되지 못했습니다.");
    }

    @PostMapping("/user-order-wish/wish")
    public ResponseEntity<String> setUserWish(@RequestBody UserWishRequestDto requestDto){
        Boolean result = orderSneakerService.setUserWish(requestDto);

        if(result){
            return ResponseEntity.ok("물품 찜 되었습니다.");
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("물품 찜 작업이 잘 되지 못했습니다.");
    }

    @GetMapping("/user-order-pay/orders")
    public Page<UserOrderResponseDto> orderPay(@RequestParam(value = "g-user-id")Integer generalUserId, Pageable pageable){
        return orderSneakerService.orderPay(generalUserId, pageable);
    }

    @PostMapping("/user-order-pay/pays")
    public ResponseEntity<String> makePayment(@RequestBody UserPayRequestDto requestDto){
        Boolean result = orderSneakerService.makePayment(requestDto);

        if(result){
            return ResponseEntity.ok("결제 성공하였습니다.");
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("해당 주문은 '주문완료' 상태가 아니어서 결제할 수 없습니다.");
    }
}
