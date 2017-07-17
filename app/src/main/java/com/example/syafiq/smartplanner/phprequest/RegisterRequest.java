package com.example.syafiq.smartplanner.phprequest;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;


public class RegisterRequest extends StringRequest{

    //<------------------- URL Connection --------------------------->
    private static final String register_URL = "http://syafiqzahir1994.hol.es/register_account.php";
    private Map<String, String> params;

    public RegisterRequest(String username,String password,String email,String phoneNo,String programme,String session, Response.Listener<String> listener){
        super(Method.POST, register_URL, listener, null);
        params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);
        params.put("email", email);
        params.put("phoneNo", phoneNo);
        params.put("programme", programme);
        params.put("session", session);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
