package com.example.forexfortune;


import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class ForexCurrency implements Serializable {

    private String symbol;

    private String name;

    private String currency;

    private static final String stockExchange = "CCY";
    private static final String exchangeShortName = "FOREX";

}