package com.CBConverter.controller;

import com.CBConverter.Converter;
import com.CBConverter.ResponseHelper;
import com.CBConverter.domain.Currency;
import com.CBConverter.domain.History;
import com.CBConverter.repository.CurrencyRepository;
import com.CBConverter.repository.HistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
public class ConverterController {

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

    @PostMapping("filter")
    public String date(@RequestParam("date")
                       @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date date, Map<String, Object> model) {
        Iterable<History> histories;
        if (date != null) {
            histories = historyRepository.findAllByDate(date);
        } else {
            histories = historyRepository.findAll();
        }
        model.put("histories", histories);
        return "history";
    }

    @GetMapping("/converter")
    public String converter(Map<String, Object> model, Model totalAmount, Model amountReceived) {
        Iterable<History> histories = historyRepository.findAll();
        Double total = historyRepository.findTotalAmountWithMaxId();
        if (total == null) total = 0d;
        Double amount = historyRepository.findAmountReceivedWithMaxId();
        if (amount == null) amount = 0d;
        totalAmount.addAttribute("totalAmount", total);
        amountReceived.addAttribute("amountReceived", amount);
        model.put("histories", histories);
        return "converter";
    }

    @PostMapping("/converter")
    public String add(@RequestParam String original_current,
                      @RequestParam String target_current, @RequestParam double amount_received) {
        History history = new History(new Converter(currencyRepository, original_current, target_current, amount_received));
        historyRepository.save(history);
        return "redirect:/converter";
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

    @GetMapping("/error")
    public String error() {
        return "error";
    }
}
