package com.pfe.ebrahim.sihaty.javaFiles;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import static com.pfe.ebrahim.sihaty.AccueilActivity.host;

/**
 * Created by ebrahim on 30/03/2018.
 */

public class MesRendezVous extends StringRequest {
    private static final String url = host+"sihaty/mesrendezvous.php";
    private Map<String, String> params;

    public MesRendezVous(String id, Response.Listener<String> listener) {
        super(Method.POST, url, listener, null);
        params = new HashMap<>();
        params.put("id",id);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
