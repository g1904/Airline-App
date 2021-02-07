package com.example.airlineapp;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

public class MySAXHandler extends DefaultHandler {

    @Override
    public void warning(SAXParseException e) throws SAXException {
        throw new SAXException("WARNING : " + e.getMessage());
    }

    @Override
    public void error(SAXParseException e) throws SAXException {
        throw new SAXException("ERROR : " + e.getMessage());
    }

    @Override
    public void fatalError(SAXParseException e) throws SAXException {
        throw new SAXException("FATAL : " + e.getMessage());
    }
}
