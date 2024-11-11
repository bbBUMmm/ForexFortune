package com.example.forexfortune;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.core.ParameterizedTypeReference;


@Service
public class StockFortuneService {

    @Value("${fmp.api.key}")
    private String apiKey;

    private static final Logger logger = LoggerFactory.getLogger(StockFortuneService.class);
    private static final String FMP_API_URL = "https://financialmodelingprep.com/api/v3/symbol/available-forex-currency-pairs";

    HttpClient httpClient = HttpClient.create()
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
            .responseTimeout(Duration.ofMillis(5000))
            .doOnConnected(conn -> conn.addHandlerLast(new ReadTimeoutHandler(5000, TimeUnit.MILLISECONDS))
                    .addHandlerLast(new WriteTimeoutHandler(5000, TimeUnit.MILLISECONDS)));

    private final WebClient client = WebClient.builder()
            .clientConnector(new ReactorClientHttpConnector(httpClient))
            .baseUrl(FMP_API_URL)
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build();

    @Cacheable(value = "forexCurrencies", unless = "#result == null")
    public Mono<List<ForexCurrency>> getAllForexCurrency() {
        return client.get()
                .uri(uriBuilder -> uriBuilder.queryParam("apikey", apiKey).build())
                .accept(MediaType.APPLICATION_JSON)
                .acceptCharset(StandardCharsets.UTF_8)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<ForexCurrency>>() {})
                .map(this::filterAndSortForexCurrencies)  // Вызов метода фильтрации и сортировки
                .doOnSuccess(response -> logger.info("Retrieved, filtered, and sorted Forex currencies from API"))
                .doOnError(error -> logger.error("Error retrieving Forex currencies: ", error));
    }

    // Метод для фильтрации валют по имени и сортировки по имени
    private List<ForexCurrency> filterAndSortForexCurrencies(List<ForexCurrency> forexCurrencies) {
        return forexCurrencies.stream()
                .filter(currency -> currency.getName().contains("EUR")) // Adjusted filtering
                .sorted(Comparator.comparing(ForexCurrency::getName))
                .collect(Collectors.toList());
    }
}
