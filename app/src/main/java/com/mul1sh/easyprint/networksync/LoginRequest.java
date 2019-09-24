package com.mul1sh.easyprint.networksync;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;


public class LoginRequest extends StringRequest {
    private static final String LOGIN_URL = "https://bodyprokenya.store/ep/login.php";
    private Map<String, String> parameters;

    public LoginRequest(String email, String password, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, LOGIN_URL, listener, errorListener);
        parameters = new HashMap<>();
        parameters.put("email", email);
        parameters.put("password", password);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }
}