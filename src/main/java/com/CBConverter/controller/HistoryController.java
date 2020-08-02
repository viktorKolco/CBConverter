package com.CBConverter.controller;

import com.CBConverter.Converter;
import com.CBConverter.ResponseHelper;
import com.CBConverter.domain.Currency;
import com.CBConverter.domain.History;
import com.CBConverter.repository.CurrencyRepository;
import com.CBConverter.repository.HistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Controller
public class HistoryController {

    @Autowired
    private HistoryRepository historyRepository;
    @Autowired
    private CurrencyRepository currencyRepository;

    @GetMapping("/history")
    public String history(Map<String, Object> model) {
        Iterable<History> histories = historyRepository.findAll();
        model.put("histories", histories);
        return "history";
    }

    @PostMapping("/history")
    public String add(@RequestParam String original_current,
                      @RequestParam String target_current, @RequestParam double amount_received) {
        History history = new History(new Converter(currencyRepository, original_current, target_current, amount_received));
        historyRepository.save(history);
        return "redirect:/history";
    }

    @GetMapping
    public String main() {
        ResponseHelper responseHelper = new ResponseHelper();
        List<Currency> list = responseHelper.getCurrenciesInfo();
        for (Currency currency : list) {
            currencyRepository.save(currency);
        }
        return "main";
    }
}
