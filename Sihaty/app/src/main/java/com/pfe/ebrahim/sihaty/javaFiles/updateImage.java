package com.pfe.ebrahim.sihaty.javaFiles;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.pfe.ebrahim.sihaty.InfosActivity;
import com.pfe.ebrahim.sihaty.LoginActivity;
import com.pfe.ebrahim.sihaty.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ResourceBundle;

import static com.pfe.ebrahim.sihaty.AccueilActivity.host;

public class updateImage extends AppCompatActivity {


    private String update_photo = host+"sihaty/uploadPhoto.php?id=";

    ImageView user_photo;
    private SharedPreferences sharedPreferences;

    String encodeimg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_image);

        sharedPreferences = getSharedPreferences(LoginActivity.MyPREFERENCES, Context.MODE_PRIVATE);
        user_photo = (ImageView) findViewById(R.id.image);
        user_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 100);
            }
        });

    }

    public void changer(View view) {
        Bitmap bitmap = ((BitmapDrawable) user_photo.getDrawable()).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 65, byteArrayOutputStream);
        encodeimg = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);

        update_photo = update_photo+sharedPreferences.getString(LoginActivity.id_key,"")+"&photo="+encodeimg;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, update_photo, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    if(success){
                        finish();
                        startActivity(new Intent(updateImage.this, InfosActivity.class));
                        Toast.makeText(getApplicationContext(),"la photo est modifié",Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(getApplicationContext(),"erreur de modification",Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, null);
        Volley.newRequestQueue(this).add(stringRequest);
    }

    public void annuler(View view) {
        startActivity(new Intent(this, InfosActivity.class));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100 && resultCode == RESULT_OK){
            Uri uri = data.getData();
            user_photo.setImageURI(uri);
        }
    }

}
