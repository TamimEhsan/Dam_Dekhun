package com.example.myeditablelist;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class AddEntries extends AppCompatActivity {

    DatabaseHelper myDB;
    Button btnAdd;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entries_add);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Add new Entry");
        editText = (EditText) findViewById(R.id.editText);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        myDB = new DatabaseHelper(this);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newEntry = editText.getText().toString();
                if(editText.length()!= 0){
                    Log.i("com.example.mylist", "run: 0 ");
                    prepforadd(newEntry);
                    editText.setText("");
                }else{
                    Toast.makeText(AddEntries.this, "You must put something in the text field!", Toast.LENGTH_LONG).show();
                }
            }
        });

    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
               break;
            default:
                break;
        }
        return true;
    }

    public void prepforadd(String data)  {

        //URI uri = new URI(data);

        try {
            URL url = new URL(data);
            String host = url.getHost();
            switch (host){
                case "www.udemy.com":
                    addUdemy(data);
                    break;
                case "www.amazon.com" :
                    addAmazon(data);
                    break;
                default:
                    addBookmars(data,host);
            }
        } catch (MalformedURLException e) {
            Toast.makeText(getApplicationContext(), "Sorry, We don't support this", Toast.LENGTH_LONG).show();
        }

    }

    public void addBookmars(final String data, final String host){
        Runnable r = new Runnable() {
            @Override
            public void run() {
                try{
                    String title;
                    Document doc = Jsoup.connect(data).userAgent("mozilla/17.0").get();
                    Element element = doc.selectFirst("title");
                    if(element!=null){
                        title = element.text();
                        AddData("bookmarks",data,title,host,"","");
                    }else{
                        AddData("bookmarks",data,data,host,"","");
                    }

                } catch(Exception e){
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Not a proper link", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        };
        Thread thread = new Thread(r);
        thread.start();
        //
    }
    public void addAmazon(final String data){
        Runnable r = new Runnable() {
            @Override
            public void run() {
                try{
                    String namee;
                    String company;
                    String cprice;
                    String sprice;
                    Log.i("com.example.mylist", "run: 1 "+data);
                    Document doc = Jsoup.connect(data).get();
                    Log.i("com.example.mylist", "run: 2 ");
                    Element name = doc.selectFirst("span[id='productTitle']");
                    if( name.text()!=null ){
                        namee = name.text();
                        Element author = doc.selectFirst("a[id='bylineInfo']");
                        if( author!=null ) {
                            company ="Product of: " + author.text();
                        } else{
                            company = "Product of Unnamed Company";
                        }
                        Element prprice = doc.select("span[id='priceblock_ourprice'],span[id='priceblock_dealprice']").first();
                        if(prprice!=null){
                            cprice = "Product Price: "+prprice.text();
                        } else{
                            cprice = "Product Price: Error";
                        }
                        Element temp2 = doc.select("#ourprice_shippingmessage > span:nth-child(3),#dealprice_shippingmessage > span:nth-child(3)").first();
                        if(temp2!=null){
                            String[] words = temp2.text().split(" ");
                            sprice = "Shipping Price: " + words[1];
                        } else{
                            sprice = "Shipping price: Error";
                        }
                       // Log.i("com.example.mylist", "run: 1.5 "+name.text());
                        AddData("amazon",data,namee,company,cprice,sprice);
                        //Log.i("com.example.mylist", "run: 2");
                    } else{
                        runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(getApplicationContext(), "Not a proper link", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                } catch (Exception e){
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Not a proper link", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        };
        Thread thread = new Thread(r);
        thread.start();
    }
    public void addUdemy(final String data){
        Runnable r = new Runnable() {
            @Override
            public void run() {
                try{
                    String namee;
                    String authorr;
                    String cprice;
                    String oprice;
                    Log.i("com.example.mylist", "run: 1 "+data);
                    Document doc = Jsoup.connect(data).get();
                    Log.i("com.example.mylist", "run: 2 ");
                    Element name = doc.selectFirst("h1.clp-lead__title ");
                    if( name.text()!=null ){
                        namee = name.text();
                        Element author = doc.selectFirst("a[rel='noopener noreferrer'],a.instructor-links__link");
                        if( author!=null ) {
                            authorr = "Created By: "+author.text();
                        } else{
                            authorr = "Created By: Unnamed Author";
                        }
                        Element temp = doc.select("span.price-text__current").first();
                        if(temp!=null){
                            cprice = temp.text();
                        } else{
                            cprice = "Current price: Free";
                        }
                        Element temp2 = doc.select("span.price-text__old").first();
                        if(temp2!=null){
                            oprice = temp2.text();
                        } else{
                            oprice = "Original price: Free";
                        }
                        Log.i("com.example.mylist", "run: 1.5 "+name.text());
                        AddData("udemy",data,namee,authorr,cprice,oprice);
                        Log.i("com.example.mylist", "run: 2");
                    } else{
                        runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(getApplicationContext(), "Not a proper link", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                } catch (Exception e){
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Not a proper link", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        };
        Thread thread = new Thread(r);
        thread.start();
    }
    public void AddData(String host, String address,String name, String author, String cprice,String oprice) {
        final String folder;
        switch (host){
            case "udemy":
                folder = "Udemy!";
                break;
            case "amazon":
                folder = "Amazon!";
                break;
            default:
                folder = "Bookmarks!";
                break;
        }
        final boolean insertData = myDB.addData(host,address,name,author,cprice,oprice);
        runOnUiThread(new Runnable() {
            public void run() {
                if(insertData==true){
                    Toast.makeText(getApplicationContext(), "Data Successfully Inserted in " + folder , Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getApplicationContext(), "Something went wrong :(.", Toast.LENGTH_LONG).show();
                }
            }
        });


    }
}
