package com.pfe.ebrahim.sihaty;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    public void toContacterAvtivity(View view) {
        startActivity(new Intent(this, ContacterActivity.class));
    }

    public void toAproposActivity(View view) {
        startActivity(new Intent(this, AproposActivity.class));
    }

    public void toClientActivity(View view) {
        startActivity(new Intent(this, LoginActivity.class));
    }

    public void toAideActivity(View view) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=uvpw6_mRy58&t=8s")));
    }
}
