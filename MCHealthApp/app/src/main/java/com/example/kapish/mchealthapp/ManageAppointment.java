package com.example.kapish.mchealthapp;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import android.app.Activity;
import android.content.Context;




public class ManageAppointment extends Fragment implements ListAdapter.customButtonListener {

    private ListView listView;
    ListAdapter adapter;
    ArrayList<String> dataItems = new ArrayList<String>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_manage_appointment, container, false);

        String[] dataArray = getResources().getStringArray(R.array.listdata);
        List<String> dataTemp = Arrays.asList(dataArray);
        dataItems.addAll(dataTemp);
        listView = (ListView) v.findViewById(R.id.listView);
        adapter = new ListAdapter(getActivity(), dataItems);
        adapter.setCustomButtonListner(this);
        listView.setAdapter(adapter);
        return v;
    }



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }




    @Override
    public void onButtonClickListner(int position, String value) {

        Toast.makeText(getActivity(), "Button click " + value,
                Toast.LENGTH_SHORT).show();
    }




}
