package com.pfe.ebrahim.sihaty;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.pfe.ebrahim.sihaty.javaFiles.MyCalendar;
import com.pfe.ebrahim.sihaty.javaFiles.addRDV;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import static com.pfe.ebrahim.sihaty.AccueilActivity.host;

public class DemanderRdvActivity extends AppCompatActivity {

    private EditText choisirDate;
    private Button validerDate;
    private SharedPreferences sharedPreferences;

    private String specialites_URL = host+"sihaty/specialites.php";
    private String jours_URL = host+"sihaty/jourNonDesponible.php?jour=";
    private String notif_URL = host+"sihaty/notifications.php?id=";

    private String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demander_rdv);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Add back button to action bar

        ((BottomNavigationView) findViewById(R.id.navigation)).setVisibility(View.GONE);


        sharedPreferences = getSharedPreferences(LoginActivity.MyPREFERENCES, Context.MODE_PRIVATE);
        choisirDate = (EditText) findViewById(R.id.choisirDate);
        validerDate = (Button) findViewById(R.id.bu_validerDate);

        demanderRDV();

    }

    @Override
    public boolean onSupportNavigateUp(){
        startActivity(new Intent(this, RDVActivity.class));
        finish();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.actionbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.item_deconnecter:
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.commit();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
                return true;
            case R.id.item_home:
                startActivity(new Intent(getApplicationContext(), AccueilActivity.class));
                return true;
            case R.id.item_notifications:
                startActivity(new Intent(getApplicationContext(),NotificationsActivity.class));
                return true;
            case R.id.item_refresh:
                finish();
                startActivity(getIntent());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        final MenuItem notificationsItem = menu.findItem(R.id.item_notifications);

        notif_URL = notif_URL+sharedPreferences.getString(LoginActivity.id_key,"");

        StringRequest stringRequest = new StringRequest(Request.Method.GET, notif_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    String n_notif = (jsonArray.getJSONObject(1)).getString("n_notif");
                    int count = Integer.parseInt(n_notif);
                    if(count>0) notificationsItem.setIcon(ContextCompat.getDrawable(getApplicationContext(), R.drawable.notification));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, null);
        Volley.newRequestQueue(this).add(stringRequest);

        return super.onPrepareOptionsMenu(menu);
    }

    public void demanderRDV(){

        final ArrayList<String> spinnerArray =  new ArrayList<String>();

        final Spinner sItems = (Spinner) findViewById(R.id.planets_spinner);

        StringRequest spec = new StringRequest(Request.Method.GET, specialites_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for(int i=0; i<jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        spinnerArray.add(jsonObject.getString("specialite"));
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                            getApplicationContext(), android.R.layout.simple_spinner_item, spinnerArray);

                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    sItems.setAdapter(adapter);

                } catch(JSONException e) {
                    e.printStackTrace();
                }
            }
        }, null);
        Volley.newRequestQueue(this).add(spec);

        final String[] itemSelected = new String[1];

        sItems.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                ((TextView) selectedItemView).setTextColor(Color.RED);
                itemSelected[0] = spinnerArray.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }

        });

        choisirDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int year = Calendar.getInstance().get(Calendar.YEAR);
                int month = Calendar.getInstance().get(Calendar.MONTH);
                int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

                MyCalendar datePickerDialog = new MyCalendar(DemanderRdvActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, final int year, final int month, final int dayOfMonth) {
                        jours_URL = jours_URL+year+"-"+(month+1)+"-"+dayOfMonth;
                        StringRequest request = new StringRequest(Request.Method.GET, jours_URL, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    boolean success = jsonObject.getBoolean("success");
                                    if(success){
                                        int count = Integer.parseInt(jsonObject.getString("n_rdv"));
                                        if(count <= 10){
                                            choisirDate.setText(year+"-"+(month+1)+"-"+dayOfMonth);
                                        }else{
                                            Toast.makeText(getApplicationContext(),"Ce jour est plein",Toast.LENGTH_LONG).show();
                                        }
                                    }else{
                                        Toast.makeText(getApplicationContext(),"Erreur",Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, null);
                        Volley.newRequestQueue(getApplicationContext()).add(request);
                    }
                }, year, month, day);   // give any  year , month , day values, this will be opened by default in dialog

                //datePickerDialog.setMinDate(2017, 8, 7); //arguments are   year , month , date (use for setting custom mix date)
                datePickerDialog.setMaxDate(year+1, month+1, day);  //arguments are   year , month , date (use for setting custom max date)

                datePickerDialog.setTodayAsMinDate();   // sets today's date as min date
                // datePickerDialog.setTodayAsMaxDate();    // sets today's date as max date

                datePickerDialog.show();
            }
        });

        validerDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRDV n_rdv = new addRDV(
                        sharedPreferences.getString(LoginActivity.id_key,""),
                        choisirDate.getText().toString(),itemSelected[0],
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    boolean success = jsonObject.getBoolean("success");
                                    if(success){
                                        Toast.makeText(getApplicationContext(),"La demande a été bien envoyée",Toast.LENGTH_LONG).show();
                                        onSupportNavigateUp();
                                    }else{
                                        Toast.makeText(getApplicationContext(),"ERROR",Toast.LENGTH_LONG).show();
                                    }
                                } catch(JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                Volley.newRequestQueue(getApplicationContext()).add(n_rdv);
            }
        });
    }
}
