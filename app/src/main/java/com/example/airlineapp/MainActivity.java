package com.example.airlineapp;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setTitle("Airline App");
    }


    public void launchSearch(View view) {
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
    }

    public void launchBrowse(View view) {
        Intent intent = new Intent( this, BrowseActivity.class);
        startActivity(intent);
    }

    public void launchAdd(View view) {
        Intent intent = new Intent(this, AddActivity.class);
        startActivity(intent);
    }

    public void launchReadMe(View view) {
        Intent intent = new Intent(this, ReadMeActivity.class);
        startActivity(intent);
    }

    public void launchHelp(View view) {
        Intent intent = new Intent(this, HelpActivity.class);
        startActivity(intent);
    }
}
