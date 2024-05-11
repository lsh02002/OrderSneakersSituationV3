package me.seho.ordersneakerssituationv3.service.CoreLogic;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeliveryManager {
    protected long salesAmount;
    protected SneakersInfo nikeSneakerInfo;
    protected SneakerPackageInfo packageInfo;

    public SneakerPackage makeSneakerPackage(){
        System.out.println("배송 관리자가 스니커를 포장합니다.");
        return new SneakerPackage();
    }

    public void sayPayment(long deliveryCost){
        System.out.printf("결제 해 주신 %d 원 감사합니다.\n", deliveryCost);
    }

    public void addSalesAmount(long cachePackage){
        salesAmount += cachePackage;
    }
}
