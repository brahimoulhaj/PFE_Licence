package com.pfe.ebrahim.sihaty.javaFiles;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import static com.pfe.ebrahim.sihaty.AccueilActivity.host;

/**
 * Created by ebrahim on 03/04/2018.
 */

public class editRDV extends StringRequest {

    private static final String url = host+"sihaty/editRDV.php";
    Map<String, String> params;

    public editRDV(String id, String jour, Response.Listener<String> listener) {
        super(Method.POST, url, listener, null);
        params = new HashMap<>();
        params.put("id",id);
        params.put("jour", jour);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
