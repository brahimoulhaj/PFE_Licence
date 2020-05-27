package com.pfe.ebrahim.sihaty;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.design.widget.BottomNavigationView;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.pfe.ebrahim.sihaty.javaFiles.Analyse;
import com.pfe.ebrahim.sihaty.javaFiles.Ordonnance;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.pfe.ebrahim.sihaty.AccueilActivity.host;

public class DossierActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;

    private String notif_URL = host+"sihaty/notifications.php?id=";
    private String ordonnance_url = host+"sihaty/ordonnance.php?dossier=";
    private String ord_url = host+"sihaty/ord/";
    private String id, date_creation, description;
    private ArrayList<Ordonnance> ordonnances;
    private TextView dateC, descr, ordon, analyse;
    private DownloadManager downloadManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dossier);
        sharedPreferences = getSharedPreferences(LoginActivity.MyPREFERENCES, Context.MODE_PRIVATE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Add back button to action bar
        id = getIntent().getExtras().getString("dos_id");
        ordonnance_url = ordonnance_url+id;
        getSupportActionBar().setTitle("Dossier N°: "+id);



        date_creation = getIntent().getExtras().getString("dos_dateC");
        description = getIntent().getExtras().getString("dos_descr");

        dateC = (TextView) findViewById(R.id.txt_dateC);
        descr = (TextView) findViewById(R.id.txt_description);
        ordon = (TextView) findViewById(R.id.txt_ordonnance);
        analyse = (TextView) findViewById(R.id.txt_analyses);


        dateC.setText(date_creation);
        descr.setText(description);

        lesOrds();

        ordon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i=0;i<ordonnances.size();i++) {
                    downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                    Uri uri = Uri.parse(ord_url + ordonnances.get(i).file);
                    DownloadManager.Request request = new DownloadManager.Request(uri);
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    Long reference = downloadManager.enqueue(request);
                }
            }
        });

        analyse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DossierActivity.this, AnalyseActivity.class);
                intent.putExtra("dos_id",id);
                startActivity(intent);
            }
        });

    }


    public void lesOrds(){
        ordonnances = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, ordonnance_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        ordonnances.add(new Ordonnance(
                                jsonObject.getString("id"),
                                jsonObject.getString("file"),
                                jsonObject.getString("dossier_id")
                        ));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, null);
        Volley.newRequestQueue(this).add(stringRequest);
    }



    //ActionBar
    @Override
    public boolean onSupportNavigateUp(){
        startActivity(new Intent(this, MesDossiersActivity.class));
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
