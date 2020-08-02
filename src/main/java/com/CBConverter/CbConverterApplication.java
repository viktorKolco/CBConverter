package com.CBConverter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

@SpringBootApplication
public class CbConverterApplication {

    public static void main(String[] args) {
        SpringApplication.run(CbConverterApplication.class, args);
    }
}
