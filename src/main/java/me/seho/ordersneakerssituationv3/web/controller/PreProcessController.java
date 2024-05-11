package me.seho.ordersneakerssituationv3.web.controller;

import lombok.AllArgsConstructor;
import me.seho.ordersneakerssituationv3.service.PreProcessService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/api")
@AllArgsConstructor
public class PreProcessController {

    PreProcessService processService;
    @GetMapping("/preprocess")
    public void process(){
        processService.PreProcess();
    }
}
