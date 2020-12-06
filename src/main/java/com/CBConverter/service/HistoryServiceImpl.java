package com.CBConverter.service;

import com.CBConverter.entities.History;
import com.CBConverter.entities.UserRole;
import com.CBConverter.repository.HistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class HistoryServiceImpl implements HistoryService {
    private final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final HistoryRepository historyRepository;

    private final ConverterService converterService;

    private final UserService userService;

    @Override
    public History getLastConvert() {
        return historyRepository.findTopByOrderByIDDesc()
                .orElse(new History("USD (Доллар США)",
                        "RUB (Российский рубль)",
                        BigDecimal.valueOf(1),
                        BigDecimal.valueOf(30),
                        LocalDateTime.now(),
                        1));
    }

    @Override
    public void addHistory(String originalCurrency, String targetCurrency, BigDecimal amountReceived) {
        if (amountReceived != null) {
            History history = new History(
                    converterService.toDescription(originalCurrency),
                    converterService.toDescription(targetCurrency),
                    amountReceived,
                    converterService.convert(originalCurrency, targetCurrency, amountReceived),
                    LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS),
                    userService.getCurrentUser().getId());
            historyRepository.save(history);
            log.info("Произведена конвертация : '{}'", history);
        } else log.info("Конвертация не была произведена. AmountReceived is null!!");
    }

    @Override
    public List<History> getByDate(String date) {
        List<History> histories;

        if (date != null && !date.equals("")) {
            LocalDate targetDate = LocalDate.parse(date, formatter);
            histories = historyRepository.findAllByDATELessThanEqualAndDATEGreaterThanEqual(
                    targetDate.plusDays(1).atStartOfDay(), targetDate.atStartOfDay());
        } else {
            histories = historyRepository.findAllByOrderByDATE();
        }
        return histories;
    }

    @Override
    public void deleteAll() {
        historyRepository.deleteAll();
    }

    @Override
    public void deleteById(int id) {
        historyRepository.deleteById(id);
    }

    @Override
    public List<History> findAllByCurrentUser() {
        return userService.getCurrentUser().getRole() == UserRole.ADMIN
                ? historyRepository.findAllByOrderByDATE()
                : historyRepository.findAllByUSERIDOrderByDATE(userService.getCurrentUser().getId());
    }
}
