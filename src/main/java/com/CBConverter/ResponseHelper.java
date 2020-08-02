package com.CBConverter;

import com.CBConverter.domain.Currency;
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
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ResponseHelper {
    private static final List<Currency> list = new ArrayList<>();

    public List<Currency> getCurrenciesInfo() {
        String url = "http://www.cbr.ru/scripts/XML_daily.asp";
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) new URL(url).openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            assert connection != null;
            return parseXML(loadXMLFromString(toStringByInputStream(new InputStreamReader(connection.getInputStream(), "Cp1251"))));
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
            throw new AssertionError("Не получилось получить курсы валют");
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
        System.out.println(xml);
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
        System.out.println("List of currencies:");
        System.out.println();
        NodeList valutes = root.getChildNodes();
        for (int i = 0; i < valutes.getLength(); i++) {
            id = valutes.item(i).getAttributes().getNamedItem("ID").getNodeValue();
            System.out.println(id);
            Node valute = valutes.item(i);
            if (valute.getNodeType() != Node.TEXT_NODE) {
                NodeList props = valute.getChildNodes();
                for (int j = 0; j < props.getLength(); j++) {
                    Node prop = props.item(j);
                    if (prop.getNodeType() != Node.TEXT_NODE) {
                        switch (j) {
                            case (0):
                                numCode = Integer.parseInt(prop.getChildNodes().item(0).getTextContent());
                                System.out.println(prop.getNodeName() + ":" + numCode);
                                break;
                            case (1):
                                charCode = prop.getChildNodes().item(0).getTextContent();
                                System.out.println(prop.getNodeName() + ":" + charCode);
                                break;
                            case (2):
                                nominal = Integer.parseInt(prop.getChildNodes().item(0).getTextContent());
                                System.out.println(prop.getNodeName() + ":" + nominal);
                                break;
                            case (3):
                                description = prop.getChildNodes().item(0).getTextContent();
                                System.out.println(prop.getNodeName() + ":" + description);
                                break;
                            case (4):
                                value = Double.parseDouble(prop.getChildNodes().item(0).getTextContent().replace(',', '.'));
                                System.out.println(prop.getNodeName() + ":" + value);
                                break;
                            default:
                                break;
                        }
                    }
                }
            }
            System.out.println("===========>>>>");
            list.add(new Currency(id, numCode, charCode, nominal, value, description));
        }
        return list;
    }
}
