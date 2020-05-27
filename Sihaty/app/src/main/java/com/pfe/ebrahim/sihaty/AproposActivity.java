package com.pfe.ebrahim.sihaty;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class AproposActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apropos);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Add back button to action bar
    }

    //ActionBar
    @Override
    public boolean onSupportNavigateUp(){
        startActivity(new Intent(this, HomeActivity.class));
        finish();
        return true;
    }
}
