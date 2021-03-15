package com.example.airlineapp;

import android.os.Build;

import androidx.annotation.RequiresApi;

import edu.pdx.cs410J.AirlineParser;
import edu.pdx.cs410J.ParserException;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class XmlParser implements AirlineParser<Airline<Flight>> {

    private final String xmlFile;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static Airline<Flight> getAirline(String xml) throws ParserException {
        if (xml == null) return null;
        InputStream inputStream = new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8));
        Airline<Flight> airline;
        Document doc;
        boolean create;

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            builder.setErrorHandler(new MySAXHandler());

            doc = builder.parse(inputStream);
            Element root = null;
            for (int i = 0; i < doc.getChildNodes().getLength(); i++) {
                if (!(doc.getChildNodes().item(i) instanceof Element)) continue;
                if ("airline".equals(doc.getChildNodes().item(i).getNodeName()))
                    root = (Element) doc.getChildNodes().item(i);
            }
            if (root == null) throw new ParserException("Cannot find airline when parsing xml.");
            airline = parse_airline(root);
        } catch (ParserConfigurationException | SAXException | IOException | IllegalArgumentException | ParserException e) {
            throw new ParserException("Error occurred when parsing Xml file: " + e.getMessage());
        }
        return airline;
    }

    /**
     * Parses some source and returns an airline.
     *
     * @throws ParserException If the source is malformatted.
     */
    @Override
    public Airline<Flight> parse() throws ParserException {
        Airline<Flight> airline;
        Document doc;
        boolean create;

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            builder.setErrorHandler(new MySAXHandler());
            File file = new File(xmlFile);
            if (!file.exists()) {
                create = file.createNewFile();
                return null;
            }
            doc = builder.parse(file);
            Element root = null;
            for (int i = 0; i < doc.getChildNodes().getLength(); i++) {
                if (!(doc.getChildNodes().item(i) instanceof Element)) continue;
                if ("airline".equals(doc.getChildNodes().item(i).getNodeName()))
                    root = (Element) doc.getChildNodes().item(i);
            }
            if (root == null) throw new ParserException("Cannot find airline when parsing xml.");
            airline = parse_airline(root);
        } catch (ParserConfigurationException | SAXException | IOException | IllegalArgumentException e) {
            throw new ParserException("Error occurred when parsing Xml file: " + e.getMessage());
        }
        return airline;
    }

    private static Airline<Flight> parse_airline(Element root) {
        NodeList children = root.getChildNodes();
        Airline<Flight> airline;
        airline = new Airline<>(children.item(1).getFirstChild().getNodeValue());
        for (int f = 1; f < children.getLength(); f++) {
            if (!(children.item(f) instanceof Element)) continue;
            if ("flight".equals(children.item(f).getNodeName())) {
                airline.addFlight(parse_flight((Element) children.item(f), airline.getName()));
            }
        }
        return airline;
    }

    private static Flight parse_flight(Element root, String airlineName) {
        String[] args = new String[8];
        args[0] = airlineName;
        for (int i = 0; i < root.getChildNodes().getLength(); i++) {
            if (!(root.getChildNodes().item(i) instanceof Element)) continue;
            switch (root.getChildNodes().item(i).getNodeName()) {
                case "number":
                    args[1] = root.getChildNodes().item(i).getFirstChild().getNodeValue();
                    break;
                case "src":
                    args[2] = root.getChildNodes().item(i).getFirstChild().getNodeValue();
                    break;
                case "depart":
                    String yrD = null, monthD = null, dayD = null, hrD = null, minD = null;
                    NodeList date_time_d = root.getChildNodes().item(i).getChildNodes();
                    for (int j = 0; j < date_time_d.getLength(); j++) {
                        Node node = date_time_d.item(j);
                        switch (node.getNodeName()) {
                            case "date":
                                NamedNodeMap date_d = node.getAttributes();
                                for (int k = 0; k < date_d.getLength(); k++) {
                                    Node attr_d = date_d.item(k);
                                    switch (attr_d.getNodeName()) {
                                        case "day":
                                            dayD = attr_d.getNodeValue();
                                            break;
                                        case "month":
                                            monthD = Integer.toString(Integer.parseInt(attr_d.getNodeValue()) + 1);
                                            break;
                                        case "year":
                                            yrD = attr_d.getNodeValue();
                                            break;
                                    }
                                }
                                break;
                            case "time":
                                NamedNodeMap time_d = node.getAttributes();
                                for (int k = 0; k < time_d.getLength(); k++) {
                                    Node attr_d = time_d.item(k);
                                    switch (attr_d.getNodeName()) {
                                        case "hour":
                                            hrD = attr_d.getNodeValue();
                                            break;
                                        case "minute":
                                            minD = attr_d.getNodeValue();
                                            break;
                                    }
                                }
                                break;
                        }
                    }
                    args[3] = monthD + "/" + dayD + "/" + yrD;
                    args[4] = hrD + ":" + minD;
                    break;
                case "dest":
                    args[5] = root.getChildNodes().item(i).getFirstChild().getNodeValue();
                    break;
                case "arrive":
                    String yrA = null, monthA = null, dayA = null, hrA = null, minA = null;
                    NodeList date_time_a = root.getChildNodes().item(i).getChildNodes();
                    for (int l = 0; l < date_time_a.getLength(); l++) {
                        Node node = date_time_a.item(l);
                        switch (node.getNodeName()) {
                            case "date":
                                NamedNodeMap date_a = node.getAttributes();
                                for (int k = 0; k < date_a.getLength(); k++) {
                                    Node attr_a = date_a.item(k);
                                    switch (attr_a.getNodeName()) {
                                        case "day":
                                            dayA = attr_a.getNodeValue();
                                            break;
                                        case "month":
                                            monthA = Integer.toString(Integer.parseInt(attr_a.getNodeValue()) + 1);
                                            break;
                                        case "year":
                                            yrA = attr_a.getNodeValue();
                                            break;
                                    }
                                }
                                break;
                            case "time":
                                NamedNodeMap time_a = node.getAttributes();
                                for (int k = 0; k < time_a.getLength(); k++) {
                                    Node attr = time_a.item(k);
                                    switch (attr.getNodeName()) {
                                        case "hour":
                                            hrA = attr.getNodeValue();
                                            break;
                                        case "minute":
                                            minA = attr.getNodeValue();
                                            break;
                                    }
                                }
                                break;
                        }
                    }
                    args[6] = monthA + "/" + dayA + "/" + yrA;
                    args[7] = hrA + ":" + minA;
                    break;
            }
        }
        return new Flight(args, false);
    }

    public XmlParser(String xmlFile) { this.xmlFile = xmlFile; }
}
