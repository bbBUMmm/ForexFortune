package com.example.forexfortune;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:63342")
public class StockFortuneController {
    private final StockFortuneService stockFortuneService;

    public StockFortuneController(StockFortuneService stockFortuneService) {
        this.stockFortuneService = stockFortuneService;
    }

    @GetMapping(value = "/allForexStocks")
    public Mono<List<ForexCurrency>> getAllForexStocks() {
        return stockFortuneService.getAllForexCurrency();
    }


    @GetMapping(value = "/returnHealth")
    public String returnHealth() {
        return "It is healthy!";
    }

}