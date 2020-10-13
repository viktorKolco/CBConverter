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
        Iterable<History> histories = historyRepository.findAllByOrderByDATE();
        model.put("histories", histories);
        return "history";
    }

    @PostMapping("filter")
    public String date(@RequestParam("date")
                       @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date date, Map<String, Object> model) {
        Iterable<History> histories;
        if (date != null) {
            histories = historyRepository.findAllByDATE(date);
        } else {
            histories = historyRepository.findAllByOrderByDATE();
        }
        model.put("histories", histories);
        return "history";
    }

    @GetMapping("/converter")
    public String converter(Model amountReceived, Model totalAmount, Model originalCurrency,
                            Model targetCurrency, Model originalChar, Model targetChar) {
        //todo: вынести отсюда
        History lastConvert = historyRepository.findTopByOrderByIDDesc().orElseThrow();
        List<Currency> list = responseService.getCurrenciesInfo();
        currencyRepository.saveAll(list);
        BigDecimal total = lastConvert.getTOTAL_AMOUNT();
        if (total == null) total = new BigDecimal(0);
        BigDecimal amount = lastConvert.getAMOUNT_RECEIVED();
        if (amount == null) amount = new BigDecimal(0);
        totalAmount.addAttribute("totalAmount", total);
        amountReceived.addAttribute("amountReceived", amount);
        String original = lastConvert.getORIGINAL_CURRENCY();
        String target = lastConvert.getTARGET_CURRENCY();
        if (original == null) original = "Введите валюту";
        if (target == null) target = "Введите валюту";
        originalCurrency.addAttribute("originalCurrency", original);
        targetCurrency.addAttribute("targetCurrency", target);
        originalChar.addAttribute("originalChar", original.substring(0, 3));
        targetChar.addAttribute("targetChar", target.substring(0, 3));
        return "converter";
    }

    @PostMapping("/converter")
    public String add(@RequestParam String postOriginalCurrency,
                      @RequestParam String postTargetCurrency, @RequestParam BigDecimal postAmountReceived) {
        History history = new History(
                converterService.toDescription(postOriginalCurrency),
                converterService.toDescription(postTargetCurrency),
                postAmountReceived,
                converterService.convert(postOriginalCurrency, postTargetCurrency, postAmountReceived),
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
