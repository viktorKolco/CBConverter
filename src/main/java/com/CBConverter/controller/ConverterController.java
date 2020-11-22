package com.CBConverter.controller;

import com.CBConverter.entities.History;
import com.CBConverter.repository.HistoryRepository;
import com.CBConverter.service.ConverterService;
import com.CBConverter.service.ResponseService;
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

    private final ResponseService responseService;

    @GetMapping("/history")
    public String history(Map<String, Object> model) {
        Iterable<History> histories = historyRepository.findAllByOrderByDATE();
        model.put("histories", histories);
        return "history";
    }

    //fixMe: поиск по дате не работает
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
        History lastConvert = historyRepository.findTopByOrderByIDDesc()
                .orElse(new History("USD (Доллар США)",
                        "RUB (Российский рубль)",
                        BigDecimal.valueOf(1),
                        BigDecimal.valueOf(30),
                        LocalDateTime.now()));
        BigDecimal total = lastConvert.getTOTAL_AMOUNT();
        BigDecimal amount = lastConvert.getAMOUNT_RECEIVED();
        totalAmount.addAttribute("totalAmount", total);
        amountReceived.addAttribute("amountReceived", amount);
        String original = lastConvert.getORIGINAL_CURRENCY();
        String target = lastConvert.getTARGET_CURRENCY();
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

    @PostMapping("deleteAll")
    public String deleteAll() {
        historyRepository.deleteAll();
        return "redirect:/history";
    }

    @PostMapping("delete")
    public String delete(@RequestParam("id") Integer id) {
        historyRepository.deleteById(id);
        return "redirect:/history";
    }

    @GetMapping("refresh")
    public String refresh() {
        log.info("Обновление курсов валют по требованию пользователя");
        responseService.getCurrenciesInfo();
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
