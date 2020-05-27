package com.pfe.ebrahim.sihaty.javaFiles;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import static com.pfe.ebrahim.sihaty.AccueilActivity.host;

/**
 * Created by ebrahim on 02/04/2018.
 */

public class addRDV extends StringRequest {
    private static final String url = host+"sihaty/addRDV.php";
    Map<String, String> params;

    public addRDV(String patient_id, String jour, String specialite, Response.Listener<String> listener) {
        super(Method.POST, url, listener, null);
        params = new HashMap<>();
        params.put("patient",patient_id);
        params.put("jour", jour);
        params.put("specialite", specialite);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
