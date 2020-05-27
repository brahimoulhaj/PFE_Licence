package com.pfe.ebrahim.sihaty;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.pfe.ebrahim.sihaty.javaFiles.MyCalendar;
import com.pfe.ebrahim.sihaty.javaFiles.addUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class RegisterActivity extends AppCompatActivity {

    private EditText NomPrenom, Cin, Telephone, date_naissance;
    private RadioButton homme, femme;
    private Button Inscrire;
    private TextView annuler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();//hide actionbar
        getWindow().setFlags(//set titlebar transparent
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        );

        NomPrenom = (EditText) findViewById(R.id.NomPrenom);
        Cin = (EditText) findViewById(R.id.cin_register);
        Telephone = (EditText) findViewById(R.id.phone_register);
        date_naissance = (EditText) findViewById(R.id.date_naissance);
        femme = (RadioButton) findViewById(R.id.sexe_femme);
        homme = (RadioButton) findViewById(R.id.sexe_homme);
        Inscrire = (Button) findViewById(R.id.bu_inscription);
        annuler = (TextView) findViewById(R.id.Annuler);

        setDate_naissance();
        inscription();
        annulerInscription();

    }

    private void setDate_naissance(){
        date_naissance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int year = Calendar.getInstance().get(Calendar.YEAR);
                int month = Calendar.getInstance().get(Calendar.MONTH);
                int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

                MyCalendar datePickerDialog = new MyCalendar(RegisterActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        date_naissance.setText(year+"-"+(month+1)+"-"+dayOfMonth);
                    }
                }, year-18, month, day);   // give any  year , month , day values, this will be opened by default in dialog

                datePickerDialog.setMinDate(1930, 1, 1); //arguments are   year , month , date (use for setting custom mix date)
                datePickerDialog.setMaxDate(year-18, 12, 31);  //arguments are   year , month , date (use for setting custom max date)

                //datePickerDialog.setTodayAsMinDate();   // sets today's date as min date
                //datePickerDialog.setTodayAsMaxDate();    // sets today's date as max date

                datePickerDialog.show();
            }
        });
    }
    private void inscription(){
        final String nom_prenom = NomPrenom.getText().toString();
        final String cin = Cin.getText().toString();
        final String telephone = Telephone.getText().toString();
        String sexe =(homme.isChecked())? "m" : (femme.isChecked())? "f" : "" ;
        final String naissance = date_naissance.getText().toString();

        Inscrire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String nom_prenom = NomPrenom.getText().toString();
                final String cin = Cin.getText().toString();
                final String telephone = Telephone.getText().toString();
                String sexe =(homme.isChecked())? "m" : (femme.isChecked())? "f" : "" ;
                final String naissance = date_naissance.getText().toString();

                Response.Listener<String> listner = new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if(success){
                                Intent intent = new Intent(RegisterActivity.this, SimpleUserActivity.class);
                                intent.putExtra("fullname", nom_prenom);
                                intent.putExtra("cin", cin);
                                startActivity(intent);
                                finish();
                            }else {
                                String error = jsonResponse.getString("error");
                                if(error.equals("ERROR_EMPTY")) Toast.makeText(getApplicationContext(), "Entrez vos informations.", Toast.LENGTH_SHORT).show();
                                if(error.equals("ERROR_CIN")) Toast.makeText(getApplicationContext(), "Ce N° de CIN est déjà utilisé.", Toast.LENGTH_SHORT).show();
                                if(error.equals("ERROR_TELEPHONE")) Toast.makeText(getApplicationContext(), "Ce N° de téléphone est déjà utilisé.", Toast.LENGTH_SHORT).show();
                                if(error.equals("INVALID_CIN")) Toast.makeText(getApplicationContext(), "CIN invalide.", Toast.LENGTH_SHORT).show();
                                if(error.equals("INVALID_TELEPHONE")) Toast.makeText(getApplicationContext(), "Téléphone invalide.", Toast.LENGTH_SHORT).show();
                                if(error.equals("ERROR_INSERT")) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                    builder.setMessage("Voulez-vous essayer?")
                                            .setTitle("Erreur d'inscription");
                                    AlertDialog dialog = builder.create();
                                    builder.setPositiveButton("OK", null);
                                    builder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                            finish();
                                        }
                                    });
                                    builder.show();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                final addUser patient = new addUser(nom_prenom, cin, naissance, telephone, sexe, listner);
                RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                queue.add(patient);
            }
        });
    }
    private void annulerInscription(){
        annuler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });
    }
}
