package com.example.airlineapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.textfield.TextInputEditText;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;

import edu.pdx.cs410J.ParserException;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_flights);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void searchFlights(View view) {
        try {
            String airlineName = Objects.requireNonNull((
                    (TextInputEditText) findViewById(R.id.search_airline_name_input)).getText(),
                    "Airline name is required.").toString();
            if (airlineName.equals("")) throw new IllegalArgumentException("Airline name is required.");
            String src = Objects.requireNonNull((
                    (TextInputEditText) findViewById(R.id.search_departure_airport_input)).getText(),
                    "Departure airport code is required.").toString();
            if (src.equals("")) throw new IllegalArgumentException("Departure airport code is required.");
            String dest = Objects.requireNonNull((
                    (TextInputEditText) findViewById(R.id.search_arrival_airport_input)).getText(),
                    "Arrival airport code is required.").toString();
            if (dest.equals("")) throw new IllegalArgumentException("Arrival airport code is required.");
            Parser.verifyAirports(src, dest);

            File airlineFile = getFile(airlineName);
            if (airlineFile == null) {
                Intent intent = new Intent( this, FlightsActivity.class);
                startActivity(intent);
            } else {
                Airline<Flight> searchResult = getResult(airlineFile, airlineName, src, dest);
                Intent intent = new Intent( this, FlightsActivity.class);
                intent.putExtra("xml", XmlDumper.getXml(searchResult));
                startActivity(intent);
            }
        } catch (IllegalArgumentException | NullPointerException | IOException | ParserException e) {
            errorMessage(e);
        }
    }

    private void errorMessage(Exception e) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(e.getMessage() + "\nPlease try again.");
        builder.setPositiveButton("Okay", (dialog, which) -> {});
        AlertDialog alert = builder.create();
        alert.show();
    }

    // get the search results in an airline object
    private Airline<Flight> getResult(File airlineFile, String airlineName, String src, String dest)
            throws IOException, ParserException {
        Airline<Flight> searchResult;
        searchResult = new Airline<>(airlineName);
        BufferedReader reader = new BufferedReader(new FileReader(airlineFile.getAbsolutePath()));
        StringBuilder stringBuilder = new StringBuilder();
        char[] buffer = new char[10];
        while (reader.read(buffer) != -1) {
            stringBuilder.append(new String(buffer));
            buffer = new char[10];
        }
        reader.close();
        String xml = stringBuilder.toString();
        for (Flight flight : Objects.requireNonNull(XmlParser.getAirline(xml)).getFlights()) {
            if (flight.getSrc().equalsIgnoreCase(src) && flight.getDest().equalsIgnoreCase(dest))
                searchResult.addFlight(flight);
        }
        return searchResult;
    }

    // get all the locally saved airline files
    private File getFile(String airlineName) {
        File directory = getDir(getResources().getString(R.string.dirName), MODE_PRIVATE);
        File[] airlines = directory.listFiles();
        File airlineFile = null;
        if (airlines != null) {
            for (File temp : airlines) {
                if (temp.getName().equals(airlineName.toLowerCase() + ".xml"))
                    airlineFile = temp;
            }
        }
        return airlineFile;
    }
}
