package com.pfe.ebrahim.sihaty;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.pfe.ebrahim.sihaty.javaFiles.Analyse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.pfe.ebrahim.sihaty.AccueilActivity.host;

public class AnalyseActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private String notif_URL = host+"sihaty/notifications.php?id=";

    private String id;
    private String ana_url = host+"sihaty/analyses/";
    private ArrayList<Analyse> analyses;
    private String analyse_url = host+"sihaty/analyse.php?dossier=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analyse);
        sharedPreferences = getSharedPreferences(LoginActivity.MyPREFERENCES, Context.MODE_PRIVATE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Add back button to action bar
        id = getIntent().getExtras().getString("dos_id");
        getSupportActionBar().setTitle("Analyses:"+id);

        lesAna();
    }

    public void lesAna(){
        analyses = new ArrayList<>();
        final ListAna listAna = new ListAna(analyses);
        ListView listeAnalyses = (ListView) findViewById(R.id.listeDesAnalyses);
        listeAnalyses.setAdapter(listAna);

        StringRequest mesAnalyses = new StringRequest(Request.Method.GET, analyse_url+id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        analyses.add(new Analyse(
                                jsonObject.getString("id"),
                                jsonObject.getString("file"),
                                jsonObject.getString("dossier_id")
                        ));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                listAna.notifyDataSetChanged();
            }
        }, null);

        Volley.newRequestQueue(this).add(mesAnalyses);
    }

    public class ListAna extends BaseAdapter {

        ArrayList<Analyse> analyses = new ArrayList<>();

        public ListAna(ArrayList<Analyse> analyses) {
            this.analyses = analyses;
        }

        @Override
        public int getCount() {
            return analyses.size();
        }

        @Override
        public Object getItem(int position) {
            return analyses.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View row = inflater.inflate(R.layout.liste_analyses, parent, false);

            final ImageView img = (ImageView) row.findViewById(R.id.analyse_img);
            String photo = analyses.get(position).file;

            Glide.with(getApplicationContext()).load(ana_url+photo).into(img);

            return row;
        }
    }

    //ActionBar
    @Override
    public boolean onSupportNavigateUp(){
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
}
