package com.CBConverter.service;

import com.CBConverter.entities.Currency;
import com.CBConverter.repository.CurrencyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class UpdateCurrencies {
    private final CurrencyRepository currencyRepository;
    private final ResponseService responseService;

    @Scheduled(fixedRate = 300000)
    private void updateCurrencies() {
        log.info("=== Обновление курсов валют по шедулеру ===");
        List<Currency> list = responseService.getCurrenciesInfo();
        if (!list.isEmpty()) {
            currencyRepository.saveAll(list);
        }
        else {
            log.error("=== Курсы валют не были обновлены ===");
        }
    }
}

