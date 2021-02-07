package com.example.airlineapp;

import android.os.Build;

import androidx.annotation.RequiresApi;

import edu.pdx.cs410J.AirlineDumper;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class PrettyPrinter implements AirlineDumper<Airline<Flight>> {
    private String fileName;

    PrettyPrinter(String fileName) {
        this.fileName = fileName;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static String print(Airline<Flight> airline) {
        if (airline != null) {
            StringBuilder result = new StringBuilder(airline.getName() + " airline contains following flight(s): ");
            List<Flight> flights = new ArrayList<>(airline.getFlights());
            flights.sort(Flight::compareTo);
            for (Flight flight : flights)
                result.append("\n    ").append(flight.prettyPrint());
            return result.toString();
        } else
            return null;
    }

    /**
     * Dumps an airline to some destination.
     *
     * @param airline The airline being written to a destination
     * @throws IOException Something went wrong while writing the airline
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void dump(Airline<Flight> airline) throws IOException {
            PrintStream out = null;
            PrintStream stdout = System.out;
            if (!fileName.equals("-")) {
                out = new PrintStream(new File(fileName));
                System.setOut(out);
            }
            if (airline != null) {
                System.out.println(airline.getName() + " airline contains following flight(s): ");
                List<Flight> flights = new ArrayList<>(airline.getFlights());
                flights.sort(Flight::compareTo);
                for (Flight flight : flights)
                    System.out.println("  " + flight.prettyPrint());
            }
            if (out != null) {
                out.close();
                System.setOut(stdout);
            }
    }
}
