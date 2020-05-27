package com.pfe.ebrahim.sihaty;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.pfe.ebrahim.sihaty.javaFiles.MesRendezVous;
import com.pfe.ebrahim.sihaty.javaFiles.MyCalendar;
import com.pfe.ebrahim.sihaty.javaFiles.RDVDelete;
import com.pfe.ebrahim.sihaty.javaFiles.RendezVous;
import com.pfe.ebrahim.sihaty.javaFiles.editRDV;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import static com.pfe.ebrahim.sihaty.AccueilActivity.host;

public class MesRDVActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private String notif_URL = host+"sihaty/notifications.php?id=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mes_rdv);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Add back button to action bar

        ((BottomNavigationView) findViewById(R.id.navigation)).setVisibility(View.GONE);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),DemanderRdvActivity.class));
            }
        });

        sharedPreferences = getSharedPreferences(LoginActivity.MyPREFERENCES, Context.MODE_PRIVATE);
        showRdvList();
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



    public void showRdvList(){
        final ArrayList<RendezVous> mesrdv = new ArrayList<RendezVous>();

        final ListResources rdv_liste = new ListResources(mesrdv);

        final ListView liste_rdv = (ListView) findViewById(R.id.listeRDV);
        liste_rdv.setAdapter(rdv_liste);

        String user_id = sharedPreferences.getString(LoginActivity.id_key, "");

        MesRendezVous mesRendezVous = new MesRendezVous(user_id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    if(jsonArray.length() == 0){
                        ((TextView) findViewById(R.id.txt_siVide)).setText("aucun rendez-vous");
                    }
                    else {
                        for(int i=0; i<jsonArray.length(); i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String id = jsonObject.getString("id");
                            String specialite = jsonObject.getString("specialite");
                            String date = "le: "+jsonObject.getString("jour")+" à "+jsonObject.getString("heure");
                            String isConfirmed = jsonObject.getString("isConfirmed");
                            String confirm = (isConfirmed).equals("0")?"Pas confirmer":" ";
                            mesrdv.add(new RendezVous(id,confirm,specialite,date));
                        }
                    }
                    rdv_liste.notifyDataSetChanged();
                } catch(JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(mesRendezVous);
    }

    public class ListResources extends BaseAdapter {

        ArrayList<RendezVous> mesrdv;
        Context context;

        public ListResources(ArrayList<RendezVous> mesrdv) {
            this.mesrdv = mesrdv;
        }

        @Override
        public int getCount() {
            return mesrdv.size();
        }

        @Override
        public Object getItem(int position) {
            return mesrdv.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View row = inflater.inflate(R.layout.liste_rdv, parent, false);
            TextView date = (TextView) row.findViewById(R.id.txt_date);
            TextView specialite = (TextView) row.findViewById(R.id.txt_specialite);
            TextView pasConfirmer = (TextView) row.findViewById(R.id.pasConfimrer);
            final RendezVous temp = mesrdv.get(position);

            date.setText(temp.date);
            specialite.setText(temp.specialite);
            pasConfirmer.setText(temp.isConfimrmed);

            ImageView delete = (ImageView) row.findViewById(R.id.delete);
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(MesRDVActivity.this);
                    alertDialog.setTitle("Supprimer un rendez-vous");
                    alertDialog.setMessage("Etes-vous sûr que vous voulez supprimer ce rendez-vous ?");

                    alertDialog.setPositiveButton("OUI", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            RDVDelete deleteRdv = new RDVDelete(temp.id, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        boolean success = jsonObject.getBoolean("success");
                                        if(success){
                                            Toast.makeText(getApplicationContext(),
                                                    "Ce rendez-vous a été annuler",
                                                    Toast.LENGTH_LONG).show();
                                            finish();
                                            startActivity(getIntent());
                                        }else{
                                            Toast.makeText(getApplicationContext(),
                                                    "Erreur !!",
                                                    Toast.LENGTH_LONG).show();
                                        }
                                    } catch(JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            });
                            RequestQueue queue = Volley.newRequestQueue(MesRDVActivity.this);
                            queue.add(deleteRdv);
                        }
                    });

                    alertDialog.setNegativeButton("NON", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    alertDialog.show();

                }
            });

            final ImageView edit = (ImageView) row.findViewById(R.id.editRDV);
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final AlertDialog.Builder editDate = new AlertDialog.Builder(MesRDVActivity.this);
                    editDate.setTitle("Modification du rendez-vous");
                    editDate.setMessage("Choisi une date:");
                    final EditText input = new EditText(MesRDVActivity.this);
                    input.setFocusable(false);
                    input.setHint("Cliquer ici !");
                    editDate.setView(input);

                    input.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int year = Calendar.getInstance().get(Calendar.YEAR);
                            int month = Calendar.getInstance().get(Calendar.MONTH);
                            int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

                            MyCalendar datePickerDialog = new MyCalendar(MesRDVActivity.this,
                                    new DatePickerDialog.OnDateSetListener() {
                                        @Override
                                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                            input.setText(year+"-"+(month+1)+"-"+dayOfMonth);
                                        }
                                    }, year, month, day);   // give any  year , month , day values, this will be opened by default in dialog

                            //datePickerDialog.setMinDate(2017, 8, 7); //arguments are   year , month , date (use for setting custom mix date)
                            datePickerDialog.setMaxDate(year+1, month+1, day);  //arguments are   year , month , date (use for setting custom max date)

                            datePickerDialog.setTodayAsMinDate();   // sets today's date as min date
                            // datePickerDialog.setTodayAsMaxDate();    // sets today's date as max date

                            datePickerDialog.show();
                        }
                    });

                    editDate.setPositiveButton("Valider", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(input.getText().toString().isEmpty()){
                                Toast.makeText(getApplicationContext(),
                                        "Entrez une date SVP !!",
                                        Toast.LENGTH_LONG).show();
                                dialog.cancel();
                            }else{
                                editRDV editrdv = new editRDV(temp.id, input.getText().toString(), new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            JSONObject js = new JSONObject(response);
                                            boolean success = js.getBoolean("success");
                                            if(success){
                                                Toast.makeText(
                                                        getApplicationContext(),
                                                        "Ce rendez-vous a été modifier",
                                                        Toast.LENGTH_LONG
                                                ).show();
                                                finish();
                                                startActivity(getIntent());
                                            }else{
                                                Toast.makeText(
                                                        getApplicationContext(),
                                                        "Erreur de modification du rendez-vous",
                                                        Toast.LENGTH_LONG
                                                ).show();
                                            }
                                        } catch(JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                                RequestQueue q = Volley.newRequestQueue(MesRDVActivity.this);
                                q.add(editrdv);
                            }
                        }
                    });

                    editDate.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    editDate.show();
                }
            });

            return row;
        }
    }
}
