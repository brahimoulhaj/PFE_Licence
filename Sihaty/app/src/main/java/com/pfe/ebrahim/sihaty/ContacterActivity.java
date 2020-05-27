package com.pfe.ebrahim.sihaty;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ContacterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacter);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Add back button to action bar
    }

    //ActionBar
    @Override
    public boolean onSupportNavigateUp(){
        startActivity(new Intent(this, HomeActivity.class));
        finish();
        return true;
    }

    public void call(View view) {
        String number = ((TextView) findViewById(R.id.tele)).getText().toString();
        String uri = "tel:"+number;
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(uri));
        startActivity(intent);
    }
}
