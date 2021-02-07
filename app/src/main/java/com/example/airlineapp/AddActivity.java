package com.example.airlineapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import edu.pdx.cs410J.ParserException;

public class AddActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_flight);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void addFlight(View view) {
        try {
            String airlineName = Objects.requireNonNull(((TextInputEditText) findViewById(R.id.airline_name_input)).getText(), "Airline name is required.").toString();
            if (airlineName.equals("")) throw new IllegalArgumentException("Airline name is required.");
            String flightNumber = Objects.requireNonNull(((TextInputEditText) findViewById(R.id.flight_number_input)).getText(), "Flight number is required.").toString();
            if (flightNumber.equals("")) throw new IllegalArgumentException("Flight number is required.");
            String src = Objects.requireNonNull(((TextInputEditText) findViewById(R.id.departure_airport_input)).getText(), "Departure airport code is required.").toString();
            if (src.equals("")) throw new IllegalArgumentException("Departure airport code is required.");
            String dest = Objects.requireNonNull(((TextInputEditText) findViewById(R.id.arrival_airport_input)).getText(), "Arrival airport code is required.").toString();
            if (dest.equals("")) throw new IllegalArgumentException("Arrival airport code is required.");
            String d_date = Objects.requireNonNull(((TextInputEditText) findViewById(R.id.departure_date_input)).getText(), "Departure date is required.").toString();
            if (d_date.equals("")) throw new IllegalArgumentException("Departure date is required.");
            String[] d_time = Objects.requireNonNull(((TextInputEditText) findViewById(R.id.departure_time_input)).getText(), "Departure time is required.").toString().split(" ");
            if (d_time.length != 2)
                throw new IllegalArgumentException("Wrong/Missing departure time input.");
            String a_date = Objects.requireNonNull(((TextInputEditText) findViewById(R.id.arrival_date_input)).getText(), "Arrival date is required.").toString();
            if (a_date.equals("")) throw new IllegalArgumentException("Arrival date is required.");
            String[] a_time = Objects.requireNonNull(((TextInputEditText) findViewById(R.id.arrival_time_input)).getText(), "Arrival time is required.").toString().split(" ");
            if (a_time.length != 2)
                throw new IllegalArgumentException("Wrong/Missing arrival time input.");
            Flight flight = new Flight(new String[] {airlineName, flightNumber, src, d_date, d_time[0], d_time[1], dest, a_date, a_time[0], a_time[1]}, false);
            File directory = getDir(getResources().getString(R.string.dirName), MODE_PRIVATE);
            File[] airlines = directory.listFiles();
            Airline<Flight> airline;
            File airlineFile = null;
            if (airlines != null) {
                for (File temp : airlines) {
                    if (temp.getName().equals(airlineName.toLowerCase() + ".xml"))
                        airlineFile = temp;
                }
            }
            if (airlineFile != null) {
                XmlParser xmlParser = new XmlParser(airlineFile.getAbsolutePath());
                XmlDumper xmlDumper = new XmlDumper(airlineFile.getAbsolutePath());
                airline = xmlParser.parse();
                airline.addFlight(flight);
                xmlDumper.dump(airline);
            } else {
                XmlDumper xmlDumper = new XmlDumper(directory.getAbsolutePath() + "/" + airlineName.toLowerCase() + ".xml");
                airline = new Airline<>(airlineName);
                airline.addFlight(flight);
                xmlDumper.dump(airline);
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Flight added successfully.");
            builder.setPositiveButton("Nice!", (dialog, which) -> {
                Intent intent = new Intent(AddActivity.this, MainActivity.class);
                startActivity(intent);
            });
            AlertDialog alert = builder.create();
            alert.show();
        } catch (NullPointerException | IllegalArgumentException | ParserException | IOException | IllegalStateException e) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(e.getMessage() + "\nPlease try again.");
            builder.setNegativeButton("Oh, jeez...", (dialog, which) -> {});
            AlertDialog alert = builder.create();
            alert.show();
        }

    }
}
