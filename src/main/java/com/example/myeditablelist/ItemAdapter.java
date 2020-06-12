package com.example.myeditablelist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ItemAdapter extends BaseAdapter {
    LayoutInflater mInflater;
    ArrayList<String> nameList;
    ArrayList<String> authorList;
    ArrayList<String> cupriceList;
    ArrayList<String> orpriceList;


    public ItemAdapter(Context c, ArrayList<String> nameList, ArrayList<String> authorList, ArrayList<String> cupriceList, ArrayList<String> orpriceList) {
        this.nameList = nameList;
        this.authorList = authorList;
        this.cupriceList = cupriceList;
        this.orpriceList = orpriceList;
        mInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return nameList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = mInflater.inflate(R.layout.adapter_view_layout,null);
        TextView nameTextView = (TextView) v.findViewById(R.id.nameTextView);
        TextView authorTextView = (TextView) v.findViewById(R.id.authorTextView);
        TextView cpTextView = (TextView) v.findViewById(R.id.cpTextView);
        TextView opTextView = (TextView) v.findViewById(R.id.opTextView);

        nameTextView.setText(nameList.get(position));
        authorTextView.setText(authorList.get(position));
        cpTextView.setText(cupriceList.get(position));
        opTextView.setText(orpriceList.get(position));
        return v;
    }
}
