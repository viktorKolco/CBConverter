package com.CBConverter.controller;

import com.CBConverter.entities.History;
import com.CBConverter.entities.UserRole;
import com.CBConverter.service.HistoryService;
import com.CBConverter.service.ResponseService;
import com.CBConverter.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ConverterController {

    private final ResponseService responseService;

    private final HistoryService historyService;

    private final UserService userService;

    @GetMapping("/history")
    public String history(Map<String, Object> model) {
        List<History> histories = historyService.findAllByCurrentUser();

        model.put("HISTORIES", histories);
        model.put("isADMIN", userService.getCurrentUser().getRole() == UserRole.ADMIN);
        model.put("isHistoryEmpty", histories.isEmpty());
        return "history";
    }

    @PostMapping("filter")
    public String date(@RequestParam("date") String date, Map<String, Object> model) {
        List<History> histories = historyService.getByDate(date);

        model.put("HISTORIES", histories);
        model.put("isADMIN", userService.getCurrentUser().getRole() == UserRole.ADMIN);
        model.put("isHistoryEmpty", histories.isEmpty());
        return "history";
    }

    @GetMapping("/converter")
    public String converter(Model amountReceived, Model totalAmount, Model originalCurrency,
                            Model targetCurrency, Model originalChar, Model targetChar) {
        History lastConvert = historyService.getLastConvert();

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
        historyService.addHistory(postOriginalCurrency, postTargetCurrency, postAmountReceived);
        return "redirect:/converter";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("deleteAll")
    public String deleteAll() {
        historyService.deleteAll();
        return "redirect:/history";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("delete")
    public String delete(@RequestParam("id") Integer id) {
        historyService.deleteById(id);
        return "redirect:/history";
    }

    @GetMapping("refresh")
    public String refresh() {
        log.info("Обновление курсов валют по требованию пользователя");
        responseService.getCurrenciesInfo();
        return "redirect:/converter";
    }

    @GetMapping
    public String main(Model userName) {
        userName.addAttribute("userName", userService.getCurrentUser().getName());
        return "main";
    }

    @GetMapping("/error")
    public String error() {
        return "error";
    }
}
