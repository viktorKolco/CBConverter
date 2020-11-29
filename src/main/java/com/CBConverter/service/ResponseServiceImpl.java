package com.CBConverter.service;

import com.CBConverter.entities.Currency;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;

@Slf4j
@Service
@RequiredArgsConstructor
public class ResponseServiceImpl implements ResponseService {

    private static final List<Currency> list = new ArrayList<>();

    @Value("${url}")
    private String url;

    public List<Currency> getCurrenciesInfo() {

        HttpURLConnection connection;
        try {
            connection = (HttpURLConnection) new URL(url).openConnection();
            Assert.notNull(connection, "Соединение не установлено");
        } catch (IOException e) {
            throw new IllegalStateException(format("Не получилось установить соединение с '%s': %s", url, e.getMessage()));
        }
        try {
            return parseXML(loadXMLFromString(toStringByInputStream(new InputStreamReader(connection.getInputStream(), "Cp1251"))));
        } catch (ParserConfigurationException | IOException | SAXException e) {
            throw new IllegalStateException(format("Не получилось получить курсы валют: %s", e.getMessage()));
        }
    }

    private String toStringByInputStream(InputStreamReader inputStreamReader) throws IOException {
        StringBuilder result = new StringBuilder();
        try (BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
            bufferedReader.lines().forEach((String line) -> result.append(line).append("\r\n"));
        }
        return result.toString();
    }

    public Document loadXMLFromString(String xml) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(xml));
        return builder.parse(is);
    }

    private List<Currency> parseXML(Document document) {
        String id;
        String charCode = null;
        int nominal = 0;
        double value = 0;
        int numCode = 0;
        String description = null;
        Node root = document.getDocumentElement();
        NodeList valutes = root.getChildNodes();
        log.debug("==================================");
        log.debug("   Информация по валютам из ЦБ:   ");
        log.debug("==================================");
        for (int i = 0; i < valutes.getLength(); i++) {
            id = valutes.item(i).getAttributes().getNamedItem("ID").getNodeValue();
            Node valute = valutes.item(i);
            if (valute.getNodeType() != Node.TEXT_NODE) {
                NodeList props = valute.getChildNodes();
                for (int j = 0; j < props.getLength(); j++) {
                    Node prop = props.item(j);
                    if (prop.getNodeType() != Node.TEXT_NODE) {
                        switch (j) {
                            case (0):
                                numCode = Integer.parseInt(prop.getChildNodes().item(0).getTextContent());
                                log.debug("numCode: '{}'", numCode);
                                break;
                            case (1):
                                charCode = prop.getChildNodes().item(0).getTextContent();
                                log.debug("charCode: '{}'", charCode);
                                break;
                            case (2):
                                nominal = Integer.parseInt(prop.getChildNodes().item(0).getTextContent());
                                log.debug("nominal: '{}'", nominal);
                                break;
                            case (3):
                                description = prop.getChildNodes().item(0).getTextContent();
                                log.debug("description: '{}'", description);
                                break;
                            case (4):
                                value = Double.parseDouble(prop.getChildNodes().item(0).getTextContent().replace(',', '.'));
                                log.debug("value: '{}'", value);
                                break;
                            default:
                                break;
                        }
                    }
                }
            }
            list.add(new Currency(id, numCode, charCode, description, nominal, new BigDecimal(value).setScale(3, RoundingMode.HALF_UP)));
            log.debug("==================================");
        }
        log.info("Курсы валют успешно обновлены");
        return list;
    }
}
