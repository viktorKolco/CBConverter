package com.CBConverter.controller;

import com.CBConverter.entities.Currency;
import com.CBConverter.entities.History;
import com.CBConverter.repository.CurrencyRepository;
import com.CBConverter.repository.HistoryRepository;
import com.CBConverter.service.ConverterService;
import com.CBConverter.service.ResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@Service
@RequiredArgsConstructor
public class ConverterController {

    private final HistoryRepository historyRepository;

    private final CurrencyRepository currencyRepository;

    private final ConverterService converterService;

    private final ResponseService responseService;

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
    public String converter(Model totalAmount, Model amountReceived) {
        List<Currency> list = responseService.getCurrenciesInfo();
        currencyRepository.saveAll(list);
        Double total = historyRepository.findTotalAmountWithMaxId();
        if (total == null) total = 0d;
        Double amount = historyRepository.findAmountReceivedWithMaxId();
        if (amount == null) amount = 0d;
        totalAmount.addAttribute("totalAmount", total);
        amountReceived.addAttribute("amountReceived", amount);
        return "converter";
    }

    @PostMapping("/converter")
    public String add(@RequestParam String originalCurrency,
                      @RequestParam String targetCurrency, @RequestParam BigDecimal postAmountReceived) {
        History history = new History(
                converterService.toDescription(originalCurrency),
                converterService.toDescription(targetCurrency),
                postAmountReceived,
                converterService.convert(originalCurrency, targetCurrency, postAmountReceived),
                Date.from(LocalDate.now()
                        .atStartOfDay()
                        .atZone(ZoneId.systemDefault())
                        .toInstant()));
        historyRepository.save(history);
        return "redirect:/converter";
    }

    @GetMapping
    public String main() {
        return "main";
    }

    @GetMapping("/error")
    public String error() {
        return "error";
    }
}
