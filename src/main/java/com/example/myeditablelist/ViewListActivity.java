package com.example.myeditablelist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;

public class ViewListActivity extends AppCompatActivity {
    DatabaseHelper myDB;
    ArrayList<String> links;
    ArrayList<String> nameList;
    ArrayList<String> authorList;
    ArrayList<String> cupriceList;
    ArrayList<String> orpriceList;
    String host;
    ListView listView;
    ItemAdapter itemAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_list);

        Intent intent = getIntent();
        int index = intent.getIntExtra("com.example.myeditablelist.ITEM_INDEX",-1);
        if (index == 1) {
            host = "amazon";
        } else {
            host = "udemy";
        }
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);

        if(index == 0){
            actionBar.setTitle("Udemy");
        } else{
            actionBar.setTitle("Amazon");
        }
        switch (index){
            case 0:
                actionBar.setTitle("Udemy");
                host = "udemy";
                break;
            case 1:
                actionBar.setTitle("Amazon");
                host = "amazon";
                break;
            default:
                actionBar.setTitle("Bookmarks");
                host = "bookmarks";
                break;
        }
        final ListView listView = (ListView) findViewById(R.id.listView);

        myDB = new DatabaseHelper(this);

        //populate an ArrayList<String> from the database and then view it
        links = new ArrayList<>();
        nameList = new ArrayList<>();
        authorList = new ArrayList<>();
        cupriceList = new ArrayList<>();
        orpriceList = new ArrayList<>();

        Cursor data = myDB.getListContents(host);
        if(data.getCount() == 0){
            Toast.makeText(this, "There are no contents in this list!",Toast.LENGTH_LONG).show();
        }else{
            while(data.moveToNext()){
                links.add(data.getString(2));
                nameList.add(data.getString(3));
                authorList.add(data.getString(4));
                cupriceList.add(data.getString(5));
                orpriceList.add(data.getString(6));
                itemAdapter = new ItemAdapter(this,nameList,authorList,cupriceList,orpriceList);
                listView.setAdapter(itemAdapter);
            }
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String link = links.get(position);
                Uri webaddress = Uri.parse(link);
                Intent gotolink = new Intent(Intent.ACTION_VIEW,webaddress);
                if( gotolink.resolveActivity(getPackageManager())!=null ){
                    startActivity(gotolink);
                }
            }
        });
        registerForContextMenu(listView);


    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.popup_menu,menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.invalidateViews();
         //String name = (String) listView.getItemAtPosition(info.position);
        String name = nameList.get(info.position);
        switch ((item.getItemId())){
            case R.id.option_delete:
                myDB.deleteName(name);
                nameList.remove(info.position);
                authorList.remove(info.position);
                cupriceList.remove(info.position);
                orpriceList.remove(info.position);
                Toast.makeText(this,"Deleted "+name,Toast.LENGTH_SHORT).show();
                return true;
            case R.id.option_cancel:
                Toast.makeText(this,"Canceled",Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onContextItemSelected(item);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        menu.findItem(R.id.additem).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.refreshitem:
                refreshdata();
                break;
            case R.id.updateitem:
                updatedata();
                break;
            case R.id.aboutitem:
                Intent intent = new Intent(getApplicationContext(),InformationActivity.class);
                intent.putExtra("com.example.myeditablelist.INFO_INDEX",0);
                startActivity(intent);
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
    public void refreshdata(){
        Cursor data = myDB.getListContents(host);
        nameList.clear();
        authorList.clear();
        cupriceList.clear();
        orpriceList.clear();
        if(data.getCount() == 0){
            Toast.makeText(getApplicationContext(), "There are no contents in this list!",Toast.LENGTH_LONG).show();
        }else{
            while(data.moveToNext()){
                nameList.add(data.getString(3));
                authorList.add(data.getString(4));
                cupriceList.add(data.getString(5));
                orpriceList.add(data.getString(6));
            }
        }
        itemAdapter.notifyDataSetChanged();
    }
    public void updatedata(){
        Runnable r = new Runnable() {
            @Override
            public void run() {
                for( String link:links ){
                    try {
                        Document doc = Jsoup.connect(link).get();
                        if( host == "udemy" ){
                            String cprice,oprice;
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
                        } else if( host == "amazon" ){
                            String cprice,sprice;
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
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),"Updated! Refresh to see changes",Toast.LENGTH_SHORT).show();

                    }
                });
            }
        };
        Thread thread = new Thread(r);
        thread.start();
    }

}