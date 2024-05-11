package me.seho.ordersneakerssituationv3.web.controller;

import lombok.AllArgsConstructor;
import me.seho.ordersneakerssituationv3.service.ProcessService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class InsertDataToDBController {

    ProcessService processService;

    @GetMapping("/insert_data_to_db")
    public void insertDataToDB(){
        processService.InsertDataToDBServiceMethod();
    }

    @GetMapping("/process")
    public void process(){
        processService.Process();
    }
}
