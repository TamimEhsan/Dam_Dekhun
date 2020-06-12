package com.example.myeditablelist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

public class InformationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        String about = "Name: Dam Dekhun \nAuthor: MD. TAMIMUL EHSAN\nVersion: 2.1\nPrimary Release Date: 13/06/2020\nDetails:\n" +
                "The main goal of this app is to decrease the time requuirement for searching through the net for regularly checked items. The app will fetch the" +
                "required data, auto sort the data and show it in a user friendly interface. Currently it only supports Udemy and Amazon and all the others are added in Bookmarks." +
                "Future updates might contain others too. Please Enjoy and send feedbacks.";
        String help = "For any updates or queries please mailto: tamimehsan99@gmail.com";
        Intent intent = getIntent();
        int index = intent.getIntExtra("com.example.myeditablelist.INFO_INDEX",-1);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        TextView infoTextView = (TextView) findViewById(R.id.informationTextView);
        switch (index){
            case 0:
                actionBar.setTitle("About");
                infoTextView.setText(about);
                break;
            case 1:
                actionBar.setTitle("Help");
                infoTextView.setText(help);
                break;
            default:
                break;
        }

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return true;
        }
    }
}