package com.pfe.ebrahim.sihaty;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.pfe.ebrahim.sihaty.javaFiles.Dossiers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.pfe.ebrahim.sihaty.AccueilActivity.host;

public class MesDossiersActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private String notif_URL = host+"sihaty/notifications.php?id=";
    private String MesDossier_URL = host+"sihaty/mesdossiers.php?id=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mes_dossiers);
        sharedPreferences = getSharedPreferences(LoginActivity.MyPREFERENCES, Context.MODE_PRIVATE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Add back button to action bar

        ((BottomNavigationView) findViewById(R.id.navigation)).setVisibility(View.GONE);

        afficheDossiers();

    }


    public void afficheDossiers(){
        final ArrayList<Dossiers> dossiers = new ArrayList<>();
        final ListDossiers listDossiers = new ListDossiers(dossiers);
        ListView listeDossiers = (ListView) findViewById(R.id.listeDossiers);
        listeDossiers.setAdapter(listDossiers);

        MesDossier_URL = MesDossier_URL+sharedPreferences.getString(LoginActivity.id_key,"");

        StringRequest mesDossiers = new StringRequest(Request.Method.GET, MesDossier_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    if(jsonArray.length() == 0) ((TextView) findViewById(R.id.aucundossier)).setText("aucun dossier");
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        dossiers.add(new Dossiers(
                                jsonObject.getString("id"),
                                jsonObject.getString("date_creation"),
                                jsonObject.getString("description")
                        ));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                listDossiers.notifyDataSetChanged();
            }
        }, null);

        Volley.newRequestQueue(this).add(mesDossiers);
    }

    public class ListDossiers extends BaseAdapter{

        ArrayList<Dossiers> dossiers = new ArrayList<>();

        public ListDossiers(ArrayList<Dossiers> dossiers) {
            this.dossiers = dossiers;
        }

        @Override
        public int getCount() {
            return dossiers.size();
        }

        @Override
        public Object getItem(int position) {
            return dossiers.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View row = inflater.inflate(R.layout.liste_dossiers, parent, false);

            TextView id = (TextView) row.findViewById(R.id.numeroDossier);
            TextView descr = (TextView) row.findViewById(R.id.description);

            CardView cardView = (CardView) row.findViewById(R.id.D_cardview);

            final String item_id = dossiers.get(position).id;
            final String item_dateC = dossiers.get(position).date_creation;
            final String item_descr = dossiers.get(position).description;

            id.setText(id.getText()+" "+item_id);
            descr.setText(item_descr);

            makeTextViewResizable(descr, 2, " ...", true);

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MesDossiersActivity.this, DossierActivity.class);
                    intent.putExtra("dos_id",item_id);
                    intent.putExtra("dos_dateC",item_dateC);
                    intent.putExtra("dos_descr",item_descr);
                    startActivity(intent);
                }
            });

            return row;
        }
    }

    public static void makeTextViewResizable(final TextView tv, final int maxLine, final String expandText, final boolean viewMore) {

        if (tv.getTag() == null) {
            tv.setTag(tv.getText());
        }
        ViewTreeObserver vto = tv.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {
                String text;
                int lineEndIndex;
                ViewTreeObserver obs = tv.getViewTreeObserver();
                obs.removeGlobalOnLayoutListener(this);
                if (maxLine == 0) {
                    lineEndIndex = tv.getLayout().getLineEnd(0);
                    text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                } else if (maxLine > 0 && tv.getLineCount() >= maxLine) {
                    lineEndIndex = tv.getLayout().getLineEnd(maxLine - 1);
                    text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                } else {
                    lineEndIndex = tv.getLayout().getLineEnd(tv.getLayout().getLineCount() - 1);
                    text = tv.getText().subSequence(0, lineEndIndex) + " " + expandText;
                }
                tv.setText(text);
                tv.setMovementMethod(LinkMovementMethod.getInstance());
            }
        });

    }

    //ActionBar
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

}
