package com.example.mspr_;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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
import com.example.mspr_.myrequest.Myrequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

class InscriptionActivity extends AppCompatActivity {

    private Button btn_valider;
    private EditText et_pseudo, et_nom, et_password, et_password2;

    private ProgressBar pb_loader;
    private RequestQueue queue;
    private Myrequest request;



    private void sendAndRequestResponse(String pseudo, String nom, String password, String password2){

        queue = Volley.newRequestQueue(this);
        StringRequest mStringRequest;

        Log.d("PRO:", "sendAndRequestResponse");
        String url = "http://mspr.fr/mspr/Inscription.php";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("PRO: Response", response);

                        Map<String, String> errors = new HashMap<>();

                        System.out.println("request onresponse");

                        try {
                            JSONObject json = new JSONObject(response);
                            Boolean error = json.getBoolean("error");

                            if (!error){
                                // L'inscription s'est bien deroulé
                                //callback.onSuccess("Inscription Reussie");
                                AppData appData = AppData.getInstance();
                                appData.setPseudo(pseudo);
                                Intent intent = new Intent(getApplicationContext(), pageAccueil.class);
                                startActivity(intent);

                            }else{
                                JSONObject messages = json.getJSONObject("message");
                                if (messages.has("pseudo")){
                                    Toast.makeText(getApplicationContext(), messages.getString("pseudo"), Toast.LENGTH_SHORT).show();
                                    //errors.put("pseudo", messages.getString("pseudo"));
                                }
                                if (messages.has("password")){
                                    Toast.makeText(getApplicationContext(), messages.getString("password"), Toast.LENGTH_SHORT).show();
                                    //errors.put("password", messages.getString("password"));
                                }
                                if (messages.has("other")){
                                    Toast.makeText(getApplicationContext(), messages.getString("other"), Toast.LENGTH_SHORT).show();
                                    //errors.put("password", messages.getString("password"));
                                }
                                //callback.inputErrors(errors);
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
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("pseudo",pseudo);
                params.put("nom",nom);
                params.put("password", password);
                params.put("password2", password2);

                return params;
            }
        };
        queue.add(postRequest);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);

        btn_valider = (Button) findViewById(R.id.btn_valider);
        et_pseudo = (EditText) findViewById(R.id.editTextTextPseudo);
        et_nom = (EditText) findViewById(R.id.editTextTextPersonNom);
        et_password = (EditText) findViewById(R.id.editTextTextPassword);
        et_password2 = (EditText) findViewById(R.id.editTextTextPassword2);


        btn_valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String pseudo = et_pseudo.getText().toString().trim();
                final String nom = et_nom.getText().toString().trim();
                final String password = et_password.getText().toString().trim();
                final String password2 = et_password2.getText().toString().trim();

                sendAndRequestResponse(pseudo, nom, password, password2);
            }
        });

    }
}