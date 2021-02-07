package com.example.airlineapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import edu.pdx.cs410J.ParserException;

public class FlightsActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flights);

        String text;
        try {
            String xml = getIntent().getStringExtra("xml");
            if (xml == null)
                text = "This airline has never been added yet.";
            else
                text = PrettyPrinter.print(XmlParser.getAirline(xml));
            View linearLayout = findViewById(R.id.flights_linear_layout);
            TextView flights = new TextView(this);
            flights.setText(text);
            ViewGroup.MarginLayoutParams layoutParams = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(ViewGroup.LayoutParams.MATCH_PARENT, 10, ViewGroup.LayoutParams.MATCH_PARENT, 10);
            flights.setLayoutParams(layoutParams);
            flights.setTextSize(25);
            ((LinearLayout) linearLayout).addView(flights);
        } catch (ParserException e) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Error: " + e.getMessage() + "\nPlease try again.");
            builder.setNegativeButton("Alright", (dialog, which) -> {});
            AlertDialog alert = builder.create();
            alert.show();
        }
    }
}
