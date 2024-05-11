package me.seho.ordersneakerssituationv3.service.CoreLogic;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import me.seho.ordersneakerssituationv3.repository.sneakers.abroadInventory.AbroadInventory;
import me.seho.ordersneakerssituationv3.repository.sneakers.abroadInventory.AbroadInventoryRepository;
import me.seho.ordersneakerssituationv3.repository.sneakers.koreaInventory.KoreaInventory;
import me.seho.ordersneakerssituationv3.repository.sneakers.koreaInventory.KoreaInventoryRepository;
import me.seho.ordersneakerssituationv3.repository.sneakers.sneaker.Sneaker;
import me.seho.ordersneakerssituationv3.repository.sneakers.sneaker.SneakerRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
public class Staff {

    private Map<String, SneakersInfo> sneakersInfoMap;
    private Map<String, Long> sneakersStockMap;
    private List<SaleInfo> saleInfoList;
    private long salesAmount;
    private boolean havingNikeSneakersInStore;

    public Staff(long salesAmount) {
        this.sneakersInfoMap = new HashMap<>();
        this.sneakersStockMap = new HashMap<>();
        this.saleInfoList = new ArrayList<>();
        this.salesAmount = salesAmount;
    }

    public boolean checkHavingNikeSneakersInStore() {
        return !sneakersStockMap.isEmpty();
    }

    public void sayPayment(long nikePrice) {
        System.out.printf("직원: 고객님 신발 주문 도와드리겠습니다. %d원 입니다\n", nikePrice);
    }

    public void addSalesAmount(long cache, Customer customer){
        salesAmount += cache;
        customer.setCache(customer.getCache()-cache);
    }

    public Sneaker offerNikeSneakers(){
        return new Sneaker();
    }

    public SneakerPackageInfo orderNikeSneakersToDeliverManager(DeliveryManager deliveryManager){
        System.out.println("직원, 배송 관리자님 나이키 스니커즈 주문(?) 부탁드립니다.");
        return deliveryManager.packageInfo;
    }

    public long calculateDeliveryCost(SneakerPackageInfo sneakerPackageInfo, Customer customer){
        long price = sneakerPackageInfo.costForDeliver;
        salesAmount += price;
        customer.setCache(customer.getCache()-price);
        return price;
    }

    public void sayNikePackageInfo(SneakerPackageInfo sneakerPackageInfo){
        System.out.printf("배송담당자: 고객님 배송은 %d 일 걸릴 예정이고 배송금액은 %d 소요되십니다.\n", sneakerPackageInfo.daysForDeliver, sneakerPackageInfo.costForDeliver);
    }

    public long returnRefund(long cache){
        salesAmount -= cache;
        return cache;
    }
}
