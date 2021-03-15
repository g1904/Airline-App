package com.example.airlineapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.android.material.textfield.TextInputEditText;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Objects;

import edu.pdx.cs410J.ParserException;

public class BrowseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);

        File directory = getDir(getResources().getString(R.string.dirName), MODE_PRIVATE);
        File[] airlines = directory.listFiles();
        View linearLayout = findViewById(R.id.browse_linear_layout);
        if (airlines != null) {
            int current = 15;
            for (File airline : airlines) {
                if (airline.getName().endsWith(".xml")) {
                    Button button = new Button(this);
                    button.setText(airline.getName().split("\\.", 2)[0]);
                    LinearLayout.LayoutParams layoutParams =
                            new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT);
                    layoutParams.setMargins(ViewGroup.LayoutParams.MATCH_PARENT, current,
                            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    layoutParams.gravity = Gravity.CENTER;
                    button.setGravity(Gravity.CENTER);
                    button.setTextSize(20);
                    button.setLayoutParams(layoutParams);
                    button.setId(current);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                Intent intent = new Intent(BrowseActivity.this, FlightsActivity.class);
                                BufferedReader reader = new BufferedReader(new FileReader(airline.getAbsolutePath()));
                                StringBuilder stringBuilder = new StringBuilder();
                                char[] buffer = new char[10];
                                while (reader.read(buffer) != -1) {
                                    stringBuilder.append(new String(buffer));
                                    buffer = new char[10];
                                }
                                reader.close();
                                String xml = stringBuilder.toString();
                                intent.putExtra("xml", xml);
                                startActivity(intent);
                            } catch (IOException e) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(BrowseActivity.this);
                                builder.setMessage("Error: " + e.getMessage() + "\nPlease try again.");
                                builder.setNegativeButton("OK", (dialog, which) -> {});
                                AlertDialog alert = builder.create();
                                alert.show();
                            }
                        }
                    });
                    ((LinearLayout) linearLayout).addView(button);
                    current += 10;
                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void searchAirline(View view) {
        try {
            String airlineName = Objects.requireNonNull((
                    (TextInputEditText) findViewById(R.id.browse_search_input)).getText(),
                    "Airline name is required.").toString();
            if (airlineName.equals("")) throw new IllegalArgumentException("Airline name is required.");
            File directory = getDir(getResources().getString(R.string.dirName), MODE_PRIVATE);
            File[] airlines = directory.listFiles();
            Airline<Flight> searchResult;
            File airlineFile = null;
            if (airlines != null) {
                for (File temp : airlines) {
                    if (temp.getName().equals(airlineName.toLowerCase() + ".xml"))
                        airlineFile = temp;
                }
            }
            if (airlineFile == null) {
                Intent intent = new Intent(this, FlightsActivity.class);
                startActivity(intent);
            } else {
                BufferedReader reader = new BufferedReader(new FileReader(airlineFile.getAbsolutePath()));
                StringBuilder stringBuilder = new StringBuilder();
                char[] buffer = new char[10];
                while (reader.read(buffer) != -1) {
                    stringBuilder.append(new String(buffer));
                    buffer = new char[10];
                }
                reader.close();
                String xml = stringBuilder.toString();
                searchResult = XmlParser.getAirline(xml);
                Intent intent = new Intent(this, FlightsActivity.class);
                intent.putExtra("xml", XmlDumper.getXml(searchResult));
                startActivity(intent);
            }
        } catch (IOException | ParserException | IllegalStateException | IllegalArgumentException e) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(e.getMessage() + "\nPlease try again.");
            builder.setPositiveButton("Oh, jeez...", (dialog, which) -> {});
            AlertDialog alert = builder.create();
            alert.show();
        }
    }
}
