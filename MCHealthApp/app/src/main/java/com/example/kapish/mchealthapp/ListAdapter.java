package com.example.kapish.mchealthapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class ListAdapter extends ArrayAdapter<String> {
    private final ArrayList<String> data;
    private final Context context;

    public ListAdapter(Context context, ArrayList<String> dataItem) {
        super(context, R.layout.child_listview, dataItem);
        this.data = dataItem;
        this.context = context;
    }

    customButtonListener customListner;

    public interface customButtonListener {
        public void onButtonClickListner(int position, String value);
    }

    public void setCustomButtonListner(customButtonListener listener) {
        this.customListner = listener;
    }

    public class ViewHolder1 {
        TextView text1;
        Button button1;
    }

    public class ViewHolder2 {
        TextView text2;
        Button button2;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder1 viewHolder1;
        ViewHolder2 viewHolder2;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.child_listview, null);
            viewHolder1 = new ViewHolder1();
            viewHolder2 = new ViewHolder2();
            viewHolder1.text1 = (TextView) convertView
                    .findViewById(R.id.childTextView1);
            viewHolder2.text2 = (TextView) convertView
                    .findViewById(R.id.childTextView2);
            viewHolder1.button1 = (Button) convertView
                    .findViewById(R.id.childButton1);
            viewHolder2.button2 = (Button) convertView
                    .findViewById(R.id.childButton2);
            convertView.setTag(viewHolder1);
            convertView.setTag(viewHolder2);
        } else {

            viewHolder1 = (ViewHolder1) convertView.getTag();
            viewHolder2 = (ViewHolder2) convertView.getTag();
        }
        final String temp = getItem(position);
        viewHolder1.text1.setText(temp);
        viewHolder1.button1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (customListner != null) {
                    customListner.onButtonClickListner(position, temp);
                }

            }
        });

        viewHolder2.text2.setText(temp);
        viewHolder2.button2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (customListner != null) {
                    customListner.onButtonClickListner(position, temp);
                }

            }
        });

        return convertView;
    }
}
