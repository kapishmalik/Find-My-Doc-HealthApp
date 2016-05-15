package com.example.kapish.mchealthapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class SearchResults extends AppCompatActivity {
    private List<Doctors> doctors = new ArrayList<Doctors>();
    String speciality=null;
    String doctor_name;
    int fees;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        populateDoctorsList();
        populateListView();
        registerClickCallback();
    }

    private void populateDoctorsList() {
        Intent intent = getIntent();
        speciality = intent.getStringExtra("speciality");
        HashMap<String,List<String>> hashMap = (HashMap<String, List<String>>) intent.getSerializableExtra("doctors");
        Iterator<Map.Entry<String, List<String>>> entries = hashMap.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<String, List<String>> entry = entries.next();
            Log.i("Doctor email", entry.getKey());
            Log.i("Doctor Name", entry.getValue().get(0));
            Log.i("Doctor Speciality", entry.getValue().get(1));
            Log.i("Doctor fees", entry.getValue().get(2));

            doctors.add(new Doctors(entry.getKey(),entry.getValue().get(0), entry.getValue().get(1), R.mipmap.dr3, Integer.parseInt(entry.getValue().get(2))));
        }
    }

    private void populateListView() {
        ArrayAdapter<Doctors> adapter = new MyListAdapter();
        ListView list = (ListView) findViewById(R.id.Doctors);
        list.setAdapter(adapter);
    }

    private void registerClickCallback() {
        ListView list = (ListView) findViewById(R.id.Doctors);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked,
                                    int position, long id) {

                Doctors clickedDoctor = doctors.get(position);

                    Intent i = new Intent(SearchResults.this,DoctorProfile.class);
                    i.putExtra("Doctorid",clickedDoctor.getDoctors_email());
                    startActivity(i);
            }
        });
    }

   private class MyListAdapter extends ArrayAdapter<Doctors> {
        public MyListAdapter() {
            super(SearchResults.this, R.layout.doctors_list, doctors);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.doctors_list, parent, false);
            }

            Doctors currentDoctor = doctors.get(position);

            ImageView imageView = (ImageView) itemView.findViewById(R.id.item_icon);
            imageView.setImageResource(currentDoctor.getIconID());

            TextView doctors_name = (TextView) itemView.findViewById(R.id.item_txtDoctorName);
            doctors_name.setText("Dr. "+currentDoctor.getDoctors_name());

            TextView speciality = (TextView) itemView.findViewById(R.id.item_txtSpeciality);
            speciality.setText("" +currentDoctor.getSpeciality());

            TextView charge = (TextView) itemView.findViewById(R.id.item_txtCharge);
            charge.setText(currentDoctor.getCharges()+"/-");

            return itemView;
        }
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
