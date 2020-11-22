package com.CBConverter.controller;

import com.CBConverter.entities.History;
import com.CBConverter.repository.HistoryRepository;
import com.CBConverter.service.ConverterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;

@Slf4j
@Controller
@Service
@RequiredArgsConstructor
public class ConverterController {

    private final HistoryRepository historyRepository;

    private final ConverterService converterService;

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
        //fixMe: исправить для случая, если база истории пустая
        History lastConvert = historyRepository.findTopByOrderByIDDesc().orElseThrow();
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
        if (postAmountReceived != null) {
            History history = new History(
                    converterService.toDescription(postOriginalCurrency),
                    converterService.toDescription(postTargetCurrency),
                    postAmountReceived,
                    converterService.convert(postOriginalCurrency, postTargetCurrency, postAmountReceived),
                    LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
            historyRepository.save(history);
            log.info("Произведена конвертация : '{}'", history);
        }
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
