package com.pfe.ebrahim.sihaty.javaFiles;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import static com.pfe.ebrahim.sihaty.AccueilActivity.host;

/**
 * Created by ebrahim on 3/24/2018.
 */

public class loginUser extends StringRequest {
    private static final String url =host+"sihaty/login.php";
    private Map<String ,String> params;

    public loginUser(String telephone, String cin, Response.Listener<String> listener) {
        super(Method.POST, url, listener, null);
        params = new HashMap<>();
        params.put("telephone",telephone);
        params.put("cin",cin);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
