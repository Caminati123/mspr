package com.example.mspr_;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mspr_.R;
import com.example.mspr_.myrequest.Myrequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

class ConnexionActivity extends AppCompatActivity {

    private Button btn_valider;
    private EditText et_pseudo, et_password;
    private RequestQueue queue;
    private Myrequest request;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion);

        btn_valider = (Button) findViewById(R.id.btn_valider);
        et_pseudo = (EditText) findViewById(R.id.editTextTextPseudo);
        et_password = (EditText) findViewById(R.id.editTextTextPassword);

        queue = VolleySingleton.getInstance(this).getRequestQueue();
        request = new Myrequest(this, queue);

        btn_valider.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                final String pseudo = et_pseudo.getText().toString().trim();
                final String password = et_password.getText().toString().trim();

                sendAndRequestResponse(pseudo, password);
            }
        });
    }

    private void sendAndRequestResponse(String pseudo, String password){

        queue = Volley.newRequestQueue(this);
        StringRequest mStringRequest;


        String url = "http://mspr.fr/mspr/Connexion.php?pseudo="+pseudo+"&password="+password;
        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);

                        Map<String, String> errors = new HashMap<>();

                        System.out.println("request onresponse");

                        try {
                            JSONObject json = new JSONObject(response);
                            Boolean error = json.getBoolean("error");

                            if (error == false){
                                // L'inscription s'est bien deroulé
                                //callback.onSuccess("Inscription Reussie");
                                AppData appData = AppData.getInstance();
                                appData.setPseudo(pseudo);

                                Intent intent = new Intent(getApplicationContext(), PageAccueil.class);
                                startActivity(intent);

                            }else{
                                JSONObject messages = json.getJSONObject("message");
                                if (messages.has("other")){
                                    Toast.makeText(getApplicationContext(), messages.getString("other"), Toast.LENGTH_SHORT).show();
                                    //errors.put("password", messages.getString("password"));
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        // error
                        Log.d("Error.Response", volleyError.toString());
                        if (volleyError instanceof TimeoutError || volleyError instanceof NoConnectionError) {
                            Toast.makeText(getApplicationContext(), "No Connection/Communication Error!", Toast.LENGTH_SHORT).show();
                        } else if (volleyError instanceof AuthFailureError) {
                            Toast.makeText(getApplicationContext(), "Authentication/ Auth Error!", Toast.LENGTH_SHORT).show();
                        } else if (volleyError instanceof ServerError) {
                            Toast.makeText(getApplicationContext(), "Server Error!", Toast.LENGTH_SHORT).show();
                        } else if (volleyError instanceof NetworkError) {
                            Toast.makeText(getApplicationContext(), "Network Error!", Toast.LENGTH_SHORT).show();
                        } else if (volleyError instanceof ParseError) {
                            Toast.makeText(getApplicationContext(), "Parse Error!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        ) {
        };
        queue.add(postRequest);
    }
}