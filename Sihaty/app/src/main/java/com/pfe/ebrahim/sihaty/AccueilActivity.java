/*
* Par Brahim OULHAJ et Chaima EL BAZ
*/
package com.pfe.ebrahim.sihaty;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.pfe.ebrahim.sihaty.javaFiles.load;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AccueilActivity extends AppCompatActivity {

    public static final String host = /*"http://192.168.56.1/";//*/ "http://projetpfe.000webhostapp.com/";

    private TextView user;
    private SharedPreferences preferences;
    private CardView infos, dossiers, rendezvous;
    private String user_id;
    private String notif_URL = host+"sihaty/notifications.php?id=";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil);

        ((BottomNavigationView) findViewById(R.id.navigation)).setVisibility(View.GONE);

        navigation();
        showUserName();
        setOnClickEvents();

    }

    private void showUserName(){
        user = (TextView) findViewById(R.id._username);
        preferences = getSharedPreferences(LoginActivity.MyPREFERENCES, Context.MODE_PRIVATE);
        user_id = preferences.getString(LoginActivity.id_key,"");
        //-------------LOAD DATA--------------//
        load load_data = new load(user_id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject json = new JSONObject(response);
                    JSONObject jsonObject = json.getJSONObject("patient");
                    String fullname = jsonObject.getString("nom_prenom");
                    String sexe = jsonObject.getString("sexe");

                    String s = (sexe.equals("m"))? "M":"Mlle";
                    ((TextView) findViewById(R.id._username)).setText(s+"."+fullname);

                } catch(JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(load_data);
        //-----------------------------------//
    }
    private void setOnClickEvents(){
        infos = (CardView) findViewById(R.id.info_personel);
        dossiers = (CardView) findViewById(R.id.dossiers);
        rendezvous = (CardView) findViewById(R.id.rendezvous);

        infos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), InfosActivity.class));
            }
        });
        dossiers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MesDossiersActivity.class));
            }
        });
        rendezvous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RDVActivity.class));
            }
        });
    }
    protected void navigation(){
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_infos:
                        return true;
                    case R.id.navigation_dossiers:
                        return true;
                    case R.id.navigation_rdv:
                        return true;
                }
                return false;
            }
        });
    }

    //My Menu
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
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.commit();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
                return true;
            case R.id.item_home:
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
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

        notif_URL = notif_URL+preferences.getString(LoginActivity.id_key,"");

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
    //End My Menu

}
