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
                    parse_date_time(args, root, i, 3, 4);
                    break;
                case "dest":
                    args[5] = root.getChildNodes().item(i).getFirstChild().getNodeValue();
                    break;
                case "arrive":
                    parse_date_time(args, root, i, 6, 7);
                    break;
            }
        }
        return new Flight(args, false);
    }

    private static void parse_date_time(String[] args, Element root , int i , int date_position, int time_position) {
        String yr = null, month = null, day = null, hr = null, min = null;
        NodeList date_time = root.getChildNodes().item(i).getChildNodes();
        for (int j = 0; j < date_time.getLength(); j++) {
            Node node = date_time.item(j);
            switch (node.getNodeName()) {
                case "date":
                    NamedNodeMap date = node.getAttributes();
                    for (int k = 0; k < date.getLength(); k++) {
                        Node attr = date.item(k);
                        switch (attr.getNodeName()) {
                            case "day":
                                day = attr.getNodeValue();
                                break;
                            case "month":
                                month = Integer.toString(Integer.parseInt(attr.getNodeValue()) + 1);
                                break;
                            case "year":
                                yr = attr.getNodeValue();
                                break;
                        }
                    }
                    break;
                case "time":
                    NamedNodeMap time = node.getAttributes();
                    for (int k = 0; k < time.getLength(); k++) {
                        Node attr = time.item(k);
                        switch (attr.getNodeName()) {
                            case "hour":
                                hr = attr.getNodeValue();
                                break;
                            case "minute":
                                min = attr.getNodeValue();
                                break;
                        }
                    }
                    break;
            }
        }
        args[date_position] = month + "/" + day + "/" + yr;
        args[time_position] = hr + ":" + min;
    }

    public XmlParser(String xmlFile) { this.xmlFile = xmlFile; }
}
