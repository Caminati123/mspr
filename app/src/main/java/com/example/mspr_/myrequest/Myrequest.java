package com.example.mspr_.myrequest;

import android.content.Context;

import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Myrequest {
    public Context context;
    private RequestQueue queue;

    public Myrequest(Context context, RequestQueue queue) {
        this.context = context;
        this.queue = queue;
    }

    public  void register(final String pseudo, final String password, final String password2, RegisterCallback callback){

        String url = "http://localhost/mspr/Inscription.php";

        System.out.println("methode register");

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Map<String, String> errors = new HashMap<>();

                System.out.println("request onresponse");

                try {
                    JSONObject json = new JSONObject(response);
                    Boolean error = json.getBoolean("error");

                    if (!error){
                        // L'inscription s'est bien deroulé
                        callback.onSuccess("Inscription Reussie");
                    }else{
                        JSONObject messages = json.getJSONObject("message");
                        if (messages.has("pseudo")){
                            errors.put("pseudo", messages.getString("pseudo"));
                        }
                        if (messages.has("password")){
                            errors.put("password", messages.getString("password"));
                        }
                        callback.inputErrors(errors);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("request onerror");
                if (error instanceof NetworkError){
                    System.out.println("Impossible de se connecter");
                    callback.onError("Impossible de se connecter");
                }else if (error instanceof VolleyError){
                    System.out.println("Oups! Une erreur s'est produite, Reessayez");
                    callback.onError("Oups! Une erreur s'est produite, Reessayez");
                }
            }
        }){
            @Override
            //protected Map<String, String> getParams() throws AuthFailureError {
            protected Map<String, String> getParams()  {
                Map<String, String> params = new HashMap<>();
                params.put("pseudo", pseudo);
                params.put("password", password);
                params.put("password2", password2);

                return params;
            }
        };

        System.out.println("avant add queue");
        queue.add(request);

    }

    public  interface RegisterCallback{
        void onSuccess(String message);
        void inputErrors(Map<String, String> errors);
        void  onError(String message);
    }
}
