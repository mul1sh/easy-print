package com.mul1sh.easyprint.networksync;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RegisterRequest  extends StringRequest {

    private static final String REGISTER_URL = "https://bodyprokenya.store/ep/register.php";
    private Map<String, String> parameters;
    public RegisterRequest(String name, String password, String mobile, String email, String photo, Response.Listener<String> listener) {
        super(Method.POST, REGISTER_URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("name", name);
        parameters.put("password", password);
        parameters.put("mobile", mobile);
        parameters.put("email", email);
        parameters.put("image", photo);

    }
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }
}
