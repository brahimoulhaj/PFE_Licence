package com.pfe.ebrahim.sihaty;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.pfe.ebrahim.sihaty.javaFiles.loginUser;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private EditText login, password;
    private CheckBox saveLoginCheckBox;
    private ProgressDialog mProgress;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String mobile_key = "mobileKey";
    public static final String cin_key = "cinKey";
    public static final String id_key = "idKey";
    SharedPreferences sharedPreferences;
    boolean isConnected = false;
    private int c_err;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login = (EditText) findViewById(R.id.input_mobile);
        password = (EditText) findViewById(R.id.input_cin);
        saveLoginCheckBox = (CheckBox)findViewById(R.id.saveLoginCheckBox);
        mProgress = new ProgressDialog(this);

        c_err = 0;

        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        mProgress.setTitle("Connexion");
        mProgress.setMessage("en cours ...");
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);

        getSupportActionBar().hide();//hide actionbar
        getWindow().setFlags(//set titlebar transparent
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        );

    }

    @Override
    protected void onResume() {
        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        if(isConnected) {
            if (sharedPreferences.contains(mobile_key)) {
                if (sharedPreferences.contains(cin_key)) {
                    Intent intent = new Intent(this, AccueilActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }else{
            Toast.makeText(getApplicationContext(),"Erreur de connexion\nActiver les données mobiles",Toast.LENGTH_LONG).show();
        }
        super.onResume();
    }

    public void connexion(View view) {
        if(isConnected) {
            final String telephone = login.getText().toString();
            final String cin = password.getText().toString();

            Response.Listener<String> listener = new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        boolean success = jsonObject.getBoolean("success");
                        if (success) {
                            c_err = 0;
                            mProgress.show();

                            JSONObject jsonObject1 = jsonObject.getJSONObject("0");
                            String user_Confirmation = jsonObject1.getString("isConfirmed");
                            String user_id = jsonObject1.getString("id");
                            String user_cin = jsonObject1.getString("cin");

                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(mobile_key, telephone);
                            editor.putString(cin_key, cin);
                            editor.putString(id_key, user_id);

                            Intent intent;
                            if (user_Confirmation.equals("1")) {
                                if (saveLoginCheckBox.isChecked()) editor.commit();
                                intent = new Intent(LoginActivity.this, AccueilActivity.class);
                            } else {
                                intent = new Intent(LoginActivity.this, SimpleUserActivity.class);
                                intent.putExtra("cin", user_cin);
                            }
                            startActivity(intent);
                            finish();
                        } else {
                            String error = jsonObject.getString("error");
                            if (error.equals("ERROR_EMPTY"))
                                Toast.makeText(getApplicationContext(), "Entez vos information.", Toast.LENGTH_LONG).show();
                            if (error.equals("ERROR_LOGIN")) {
                                c_err++;
                                Toast.makeText(getApplicationContext(), "Téléphone ou CIN est invalide.", Toast.LENGTH_LONG).show();
                                if(c_err > 2){
                                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(LoginActivity.this);
                                    alertDialog.setTitle("Erreur de connexion");
                                    alertDialog.setMessage("Veuiller conntacter votre cabinet");

                                    alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            finish();
                                            System.exit(0);
                                        }
                                    });

                                    alertDialog.show();
                                }
                            }
                            if (error.equals("ERROR_SELECT") || error.equals("ERROR_CONNECTION"))
                                Toast.makeText(getApplicationContext(), "Erreur de connexion.", Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            };


            loginUser newlogin = new loginUser(telephone, cin, listener);
            RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
            queue.add(newlogin);
        }else{
            Toast.makeText(getApplicationContext(),"Erreur de connexion\nActiver les données mobiles",Toast.LENGTH_LONG).show();
        }

    }

    public void register(View view) {
        startActivity(new Intent(this, RegisterActivity.class));
    }
}
