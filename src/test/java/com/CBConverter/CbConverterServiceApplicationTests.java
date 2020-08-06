package com.CBConverter;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.net.HttpURLConnection;
import java.net.URL;

import static java.lang.String.format;
import static org.springframework.util.Assert.isTrue;
import static org.springframework.util.Assert.notNull;

@Slf4j
@SpringBootTest
class CbConverterServiceApplicationTests {

    @Test
    void checkConnection() throws Exception {
        String url = "http://www.cbr.ru/scripts/XML_daily.asp";
        HttpURLConnection connection;
        connection = (HttpURLConnection) new URL(url).openConnection();
        notNull(connection, "Соединение не установлено. Проверьте интернет соединение");
        isTrue(connection.getResponseCode() == HttpStatus.OK.value(), format("StatusCode: %s", connection.getResponseCode()));
    }
}
