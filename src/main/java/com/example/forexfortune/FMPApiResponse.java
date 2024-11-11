package com.example.forexfortune;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Setter
@Getter
public class FMPApiResponse implements Serializable {
    private List<ForexCurrency> results;

}