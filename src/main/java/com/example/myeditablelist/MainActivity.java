package com.example.myeditablelist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        getSupportActionBar().setTitle("Dam Dekhun");


        ArrayList<String> names = new ArrayList<>();
        names.add("Udemy");
        names.add("Amazon");
        names.add("Bookmarks");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,android.R.id.text1,names);
        ListView mainListView = (ListView) findViewById(R.id.mainListView);
        mainListView.setAdapter(adapter);

        mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(),ViewListActivity.class);
                intent.putExtra("com.example.myeditablelist.ITEM_INDEX",position);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        MenuItem item = menu.findItem(R.id.refreshitem);
        item.setVisible(false);
        menu.findItem(R.id.updateitem).setVisible(false);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.additem:
                Intent intent = new Intent(getApplicationContext(), AddEntries.class);
                startActivity(intent);
                break;
            case R.id.aboutitem:
                Intent intent3 = new Intent(getApplicationContext(),InformationActivity.class);
                intent3.putExtra("com.example.myeditablelist.INFO_INDEX",0);
                startActivity(intent3);
                break;
            case R.id.helpitem:
                Intent intent2 = new Intent(getApplicationContext(),InformationActivity.class);
                intent2.putExtra("com.example.myeditablelist.INFO_INDEX",1);
                startActivity(intent2);
            default:
                break;
        }
        return true;
    }
}