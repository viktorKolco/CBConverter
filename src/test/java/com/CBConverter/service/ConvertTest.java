package com.CBConverter.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

@Slf4j
@SpringBootTest
public class ConvertTest {

    @Autowired
    private ConverterService converterService;

    @Test
    public void withoutRublesTest() {
        BigDecimal numb = BigDecimal.valueOf(1);
        Assert.assertTrue(converterService.convert("EUR", "USD", numb).compareTo(numb) > 0);
    }

    @Test
    public void withOnlyRublesTest() {
        BigDecimal numb = BigDecimal.valueOf(1);
        Assert.assertEquals(0, converterService.convert("RUB", "RUB", numb).compareTo(numb));
    }

    @Test
    public void inputRublesTest() {
        BigDecimal numb = BigDecimal.valueOf(1);
        Assert.assertTrue(converterService.convert("RUB", "USD", numb).compareTo(numb) < 0);
    }

    @Test
    public void outputRublesTest() {
        BigDecimal numb = BigDecimal.valueOf(1);
        Assert.assertTrue(converterService.convert("USD", "RUB", numb).compareTo(numb) > 0);
    }

    @Test
    public void nullInputTest() {
        BigDecimal numb = BigDecimal.valueOf(1);
        Assert.assertTrue(converterService.convert("EUR", "USD", null).compareTo(numb) > 0);
    }

}