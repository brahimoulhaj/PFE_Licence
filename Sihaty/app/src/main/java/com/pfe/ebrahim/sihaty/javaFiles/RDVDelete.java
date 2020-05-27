package com.pfe.ebrahim.sihaty.javaFiles;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import static com.pfe.ebrahim.sihaty.AccueilActivity.host;

/**
 * Created by ebrahim on 02/04/2018.
 */

public class RDVDelete extends StringRequest {

    private static final String url = host+"sihaty/RDVDelete.php";
    Map<String, String> params;

    public RDVDelete(String id, Response.Listener<String> listener) {
        super(Method.POST, url, listener, null);
        params = new HashMap<>();
        params.put("id",id);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
