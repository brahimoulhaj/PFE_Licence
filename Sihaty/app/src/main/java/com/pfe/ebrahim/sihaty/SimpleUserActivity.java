package com.pfe.ebrahim.sihaty;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.print.PrinterId;
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

public class SimpleUserActivity extends AppCompatActivity {

    private EditText choisirDate;
    private Button validerDate;
    private SharedPreferences sharedPreferences;

    private String jours_URL = host+"sihaty/jourNonDesponible.php?jour=";
    private String SIMPLE_USER_URL = host+"sihaty/simpleuser.php?cin=";

    private String user_cin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_user);

        choisirDate = (EditText) findViewById(R.id.choisirDate);
        validerDate = (Button) findViewById(R.id.bu_validerDate);

        user_cin = getIntent().getExtras().getString("cin");

        SIMPLE_USER_URL += user_cin+"&jour=";

        Toast.makeText(getApplicationContext(),user_cin,Toast.LENGTH_LONG).show();

        demanderRDV();

    }

    public void demanderRDV(){

        choisirDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int year = Calendar.getInstance().get(Calendar.YEAR);
                int month = Calendar.getInstance().get(Calendar.MONTH);
                int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

                MyCalendar datePickerDialog = new MyCalendar(SimpleUserActivity.this,
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
                                                    SIMPLE_USER_URL += choisirDate.getText().toString();
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
                StringRequest stringRequest = new StringRequest(Request.Method.GET, SIMPLE_USER_URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean s = jsonObject.getBoolean("success");
                            if(s){
                                Toast.makeText(getApplicationContext(),"Demande envoyer",Toast.LENGTH_LONG).show();
                                finish();
                                startActivity(getIntent());
                            }else{
                                Toast.makeText(getApplicationContext(),"Erreur !!",Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, null);

                Volley.newRequestQueue(getApplicationContext()).add(stringRequest);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.simpleuser, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.logout:
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
        }
        return true;
    }

}
