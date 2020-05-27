package com.pfe.ebrahim.sihaty;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import static com.pfe.ebrahim.sihaty.AccueilActivity.host;

public class RDVActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    CardView mesrdv, demanderrdv;
    private String notif_URL = host+"sihaty/notifications.php?id=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rdv);

        sharedPreferences = getSharedPreferences(LoginActivity.MyPREFERENCES,Context.MODE_PRIVATE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Add back button to action bar

        ((BottomNavigationView) findViewById(R.id.navigation)).setVisibility(View.GONE);

        mesrdv = (CardView) findViewById(R.id.mes_rdv);
        demanderrdv = (CardView) findViewById(R.id.demander_rdv);

        onClickEvent();

    }

    @Override
    public boolean onSupportNavigateUp(){
        startActivity(new Intent(this, AccueilActivity.class));
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

    public void onClickEvent(){
        mesrdv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MesRDVActivity.class));
            }
        });

        demanderrdv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), DemanderRdvActivity.class));
            }
        });
    }
}
