package com.pfe.ebrahim.sihaty;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.pfe.ebrahim.sihaty.javaFiles.notifications;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;

import static com.pfe.ebrahim.sihaty.AccueilActivity.host;

public class NotificationsActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private String notif_URL = host+"sihaty/notifications.php?id=";
    private String readAllNotif_URL = host+"sihaty/notif_makeAllAsRead.php?id=";
    private String readNotif_URL = host+"sihaty/notif_makeAsRead.php?id=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Add back button to action bar
        sharedPreferences = getSharedPreferences(LoginActivity.MyPREFERENCES, Context.MODE_PRIVATE);

        ((BottomNavigationView) findViewById(R.id.navigation)).setVisibility(View.GONE);

        showNotifications();

    }

    @Override
    public boolean onSupportNavigateUp(){
        startActivity(new Intent(this, AccueilActivity.class));
        finish();
        return true;
    }

    public void toutMarquerCommeLu(View view){
        readAllNotif_URL = readAllNotif_URL+sharedPreferences.getString(LoginActivity.id_key,"");
        StringRequest stringRequest = new StringRequest(Request.Method.GET,readAllNotif_URL,null,null);
        Volley.newRequestQueue(this).add(stringRequest);
        finish();
        startActivity(getIntent());
    }

    public void showNotifications(){
        final ArrayList<notifications> notifications = new ArrayList<>();

        final TextView count = (TextView) findViewById(R.id.txt_count_notif);

        final ListResNotif notifs = new ListResNotif(notifications);

        final ListView liste_notif = (ListView) findViewById(R.id.list_notifications);
        liste_notif.setAdapter(notifs);
        liste_notif.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item_id = (notifications.get(position)).id;
                readNotif_URL = readNotif_URL+item_id+"&patient_id="+sharedPreferences.getString(LoginActivity.id_key,"");
                StringRequest stringRequest = new StringRequest(Request.Method.GET,readNotif_URL,null,null);
                Volley.newRequestQueue(getApplicationContext()).add(stringRequest);
                finish();
                startActivity(getIntent());
            }
        });

        notif_URL = notif_URL+sharedPreferences.getString(LoginActivity.id_key,"");

        StringRequest stringRequest = new StringRequest(Request.Method.GET, notif_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    if(jsonArray.length() <= 2){
                        ((TextView) findViewById(R.id.txt_notif_siVide)).setText("aucun notification");
                    }

                    boolean success = (jsonArray.getJSONObject(0)).getBoolean("success");
                    String n_notif = (jsonArray.getJSONObject(1)).getString("n_notif");
                    count.setText("("+n_notif+")");
                    if(success){
                        for (int i=2;i<jsonArray.length();i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String id = jsonObject.getString("id");
                            String title = jsonObject.getString("title");
                            String text = jsonObject.getString("text");
                            String time = jsonObject.getString("time");
                            String seen = jsonObject.getString("seen");
                            notifications.add(new notifications(id,title,text,time,seen));

                        }
                    }else{
                        Toast.makeText(getApplicationContext(),"ERROR LOADING DATA",Toast.LENGTH_LONG).show();
                    }
                    notifs.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, null);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);
    }

    public class ListResNotif extends BaseAdapter{

        ArrayList<notifications> notifications;
        Context context;

        public ListResNotif(ArrayList<com.pfe.ebrahim.sihaty.javaFiles.notifications> notifications) {
            this.notifications = notifications;
        }

        @Override
        public int getCount() {
            return notifications.size();
        }

        @Override
        public Object getItem(int position) {
            return notifications.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View row = inflater.inflate(R.layout.list_notification, parent, false);
            TextView title = (TextView) row.findViewById(R.id.notif_title);
            TextView text = (TextView) row.findViewById(R.id.notif_content);
            TextView time = (TextView) row.findViewById(R.id.notif_time);

            final notifications temp = notifications.get(position);

            title.setText(temp.title);
            text.setText(temp.text);
            time.setText(temp.time);

            if(temp.seen.equals("0")){
                LinearLayout linearLayout = (LinearLayout) row.findViewById(R.id.new_notif);
                linearLayout.setBackgroundColor(Color.parseColor("#22007a99"));
            }

            return row;
        }
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
