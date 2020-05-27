package com.pfe.ebrahim.sihaty.javaFiles;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import static com.pfe.ebrahim.sihaty.AccueilActivity.host;

/**
 * Created by ebrahim on 3/24/2018.
 */

public class addUser extends StringRequest {

    private static final String url = host+"sihaty/register.php";
    private Map<String, String> params;

    public addUser(String fullname, String cin, String naissance,
                   String telephone, String sexe,
                   Response.Listener<String> listener) {
        super(Method.POST, url, listener, null);
        params = new HashMap<>();
        params.put("fullname", fullname);
        params.put("cin", cin);
        params.put("naissance", naissance);
        params.put("telephone", telephone);
        params.put("sexe", sexe);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}