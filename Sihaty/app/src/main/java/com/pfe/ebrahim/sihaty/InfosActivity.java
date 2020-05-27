package com.pfe.ebrahim.sihaty;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.pfe.ebrahim.sihaty.javaFiles.load;
import com.pfe.ebrahim.sihaty.javaFiles.updateImage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

import static com.pfe.ebrahim.sihaty.AccueilActivity.host;

public class InfosActivity extends AppCompatActivity {

    private ImageView user_photo, modifier_phone,
            modifier_email, modifier_adresse, modifier_photo;
    private ProgressDialog mProgress;
    private SharedPreferences sharedPreferences;

    private String img_URL = host+"sihaty/img/";
    private String updatePhone_URL = host+"sihaty/updatePhone.php?id=";
    private String updateEmail_URL = host+"sihaty/updateEmail.php?id=";
    private String updateAddress_URL = host+"sihaty/updateAddress.php?id=";
    private String notif_URL = host+"sihaty/notifications.php?id=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infos);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Add back button to action bar

        ((BottomNavigationView) findViewById(R.id.navigation)).setVisibility(View.GONE);

        mProgress = new ProgressDialog(InfosActivity.this);
        mProgress.setTitle("Processing...");
        mProgress.setMessage("Please wait...");
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);

        sharedPreferences = getSharedPreferences(LoginActivity.MyPREFERENCES, Context.MODE_PRIVATE);
        user_photo = (ImageView) findViewById(R.id.profil_photo);
        modifier_phone = (ImageView) findViewById(R.id.modifier_phone);
        modifier_email = (ImageView) findViewById(R.id.modifier_email);
        modifier_adresse = (ImageView) findViewById(R.id.modifier_adresse);

        showUserInformations();
        editUserInformations();
        updatePhoto();


    }

    public void updatePhoto(){
        user_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(InfosActivity.this, updateImage.class));
            }
        });
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

    private void showUserInformations(){
        String user_id = sharedPreferences.getString(LoginActivity.id_key,"");
        load load_data = new load(user_id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject json = new JSONObject(response);
                    JSONObject jsonObject = json.getJSONObject("patient");
                    String fullname = jsonObject.getString("nom_prenom");
                    String cin = jsonObject.getString("cin");
                    String naissance = jsonObject.getString("date_naissance");
                    String phone = jsonObject.getString("telephone");
                    String sexe = jsonObject.getString("sexe");
                    String adresse = jsonObject.getString("adresse");
                    String email = jsonObject.getString("email");
                    String photo = jsonObject.getString("photo");

                    String s = (sexe.equals("m"))? "M":"Mlle";
                    ((TextView) findViewById(R.id.nom_prenom)).setText(s+"."+fullname);
                    ((TextView) findViewById(R.id.cin)).setText(cin);
                    ((TextView) findViewById(R.id.phone)).setText(phone+"\nMobile");
                    ((TextView) findViewById(R.id.date_naissance)).setText(naissance+"\nDate de naissance");
                    ((TextView) findViewById(R.id.email)).setText((email.equals("null")?"___":email)+"\nEmail");
                    ((TextView) findViewById(R.id.adresse)).setText((adresse.equals("null")?"___":adresse)+"\nAdresse");
                    if(!photo.equals("null")){
                        Glide.with(InfosActivity.this).load(img_URL+photo)
                                .asBitmap().
                                centerCrop().
                                into(new BitmapImageViewTarget(user_photo) {
                                    @Override
                                    protected void setResource(Bitmap resource) {
                                        RoundedBitmapDrawable circularBitmapDrawable =
                                                RoundedBitmapDrawableFactory.create(InfosActivity.this.getResources(), resource);
                                        circularBitmapDrawable.setCircular(true);
                                        user_photo.setImageDrawable(circularBitmapDrawable);
                                    }
                                });
                    }
                } catch(JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(load_data);
    }
    private void editUserInformations(){

        //edit phone number
        modifier_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(InfosActivity.this);
                alertDialog.setTitle("Téléphone");
                alertDialog.setMessage("Nouveau N°:");

                final EditText input = new EditText(InfosActivity.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);
                alertDialog.setView(input);

                alertDialog.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Response.Listener<String> listener = new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            JSONObject jsonObject = new JSONObject(response);
                                            boolean success = jsonObject.getBoolean("success");
                                            if(success){
                                                Toast.makeText(getApplicationContext(),"le N° de téléphone a été mise à jour",Toast.LENGTH_LONG).show();
                                                finish();
                                                startActivity(getIntent());
                                            }else{
                                                String error = jsonObject.getString("error");
                                                if(error.equals("INVALID_TELEPHONE"))
                                                    Toast.makeText(getApplicationContext(),"N° invalide",Toast.LENGTH_LONG).show();
                                                if(error.equals("ERROR_TELEPHONE"))
                                                    Toast.makeText(getApplicationContext(),"N° déjà utilisé",Toast.LENGTH_LONG).show();
                                                else
                                                    Toast.makeText(getApplicationContext(),"Erreur de modification",Toast.LENGTH_LONG).show();
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                };
                                String user_id = sharedPreferences.getString(LoginActivity.id_key,"");
                                updatePhone_URL = updatePhone_URL+user_id+"&newphone="+input.getText().toString();
                                StringRequest stringRequest = new StringRequest(Request.Method.GET,updatePhone_URL,
                                                                                    listener,null);
                                RequestQueue queue = Volley.newRequestQueue(InfosActivity.this);
                                queue.add(stringRequest);
                            }
                        });

                alertDialog.setNegativeButton("Annuler",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                alertDialog.show();
            }
        });

        //edit address email
        modifier_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(InfosActivity.this);
                alertDialog.setTitle("Email");
                alertDialog.setMessage("Nouvelle adresse email:");

                final EditText input = new EditText(InfosActivity.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);
                alertDialog.setView(input);

                alertDialog.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Response.Listener<String> listener = new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            JSONObject jsonObject = new JSONObject(response);
                                            boolean success = jsonObject.getBoolean("success");
                                            if(success){
                                                Toast.makeText(getApplicationContext(),"l'email a été mise à jour",Toast.LENGTH_LONG).show();
                                                finish();
                                                startActivity(getIntent());
                                            }else{
                                                String error = jsonObject.getString("error");
                                                if(error.equals("INVALID_EMAIL"))
                                                    Toast.makeText(getApplicationContext(),"Email invalide",Toast.LENGTH_LONG).show();
                                                if(error.equals("EMAIL_USED"))
                                                    Toast.makeText(getApplicationContext(),"Email déjà utilisé",Toast.LENGTH_LONG).show();
                                                else
                                                    Toast.makeText(getApplicationContext(),"Erreur de modification",Toast.LENGTH_LONG).show();                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                };
                                String user_id = sharedPreferences.getString(LoginActivity.id_key,"");
                                updateEmail_URL = updateEmail_URL+user_id+"&newemail="+input.getText().toString();
                                StringRequest stringRequest = new StringRequest(Request.Method.GET,updateEmail_URL,
                                        listener,null);
                                RequestQueue queue = Volley.newRequestQueue(InfosActivity.this);
                                queue.add(stringRequest);
                            }
                        });

                alertDialog.setNegativeButton("Annuler",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                alertDialog.show();
            }
        });

        //edit address
        modifier_adresse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(InfosActivity.this);
                alertDialog.setTitle("Adresse");
                alertDialog.setMessage("Nouvelle adresse :");

                final EditText input = new EditText(InfosActivity.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);
                alertDialog.setView(input);

                alertDialog.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Response.Listener<String> listener = new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            JSONObject jsonObject = new JSONObject(response);
                                            boolean success = jsonObject.getBoolean("success");
                                            if(success){
                                                Toast.makeText(getApplicationContext(),"l'adresse a été mise à jour",Toast.LENGTH_LONG).show();
                                                finish();
                                                startActivity(getIntent());
                                            }else{
                                                Toast.makeText(getApplicationContext(),"Erreur de modification",Toast.LENGTH_LONG).show();
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                };
                                String user_id = sharedPreferences.getString(LoginActivity.id_key,"");
                                updateAddress_URL = updateAddress_URL+user_id+"&newaddress="+input.getText().toString();
                                StringRequest stringRequest = new StringRequest(Request.Method.GET,updateAddress_URL,
                                        listener,null);
                                RequestQueue queue = Volley.newRequestQueue(InfosActivity.this);
                                queue.add(stringRequest);
                            }
                        });

                alertDialog.setNegativeButton("Annuler",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                alertDialog.show();
            }
        });

    }


}
