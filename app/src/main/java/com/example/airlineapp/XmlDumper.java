package com.example.airlineapp;

import android.content.Context;

import edu.pdx.cs410J.AirlineDumper;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;

public class XmlDumper implements AirlineDumper<Airline<Flight>> {

    private final String xmlFile;

    public static String getXml(Airline<Flight> airline) {
        if (airline == null)
            throw new NullPointerException("This airline does not exist.");
        String header = "<?xml version='1.0' encoding='us-ascii'?>\n";
        header += "\n<!DOCTYPE airline\n";
        header += "          SYSTEM \"http://www.cs.pdx.edu/~whitlock/dtds/airline.dtd\">\n";
        header += "\n<airline>\n  <name>" + airline.getName() + "</name>\n";
        StringBuilder content = new StringBuilder();
        for (Flight flight : airline.getFlights())
            content.append(flight.xml());
        return header + content + "</airline>\n";
    }

    /**
     * Dumps an airline to some destination.
     *
     * @param airline The airline being written to a destination
     */
    @Override
    public void dump(Airline<Flight> airline) throws IOException {

        String header = "<?xml version='1.0' encoding='us-ascii'?>\n";
        header += "\n<!DOCTYPE airline\n";
        header += "          SYSTEM \"http://www.cs.pdx.edu/~whitlock/dtds/airline.dtd\">\n";
        header += "\n<airline>\n  <name>" + airline.getName() + "</name>\n";
        BufferedWriter bufferedWriter;
        try {
            bufferedWriter = new BufferedWriter(new FileWriter(new File(xmlFile)));
            Collection<Flight> flights = airline.getFlights();
            bufferedWriter.write(header);
            for (Flight flight : flights) {
                bufferedWriter.write(flight.xml());
            }
            bufferedWriter.write("</airline>");
            bufferedWriter.close();
        } catch (IOException e) {
            throw new IOException("Something went wrong while writing the airline to xml file.");
        }
    }

    public XmlDumper(String xmlFile) { this.xmlFile = xmlFile; }

}
