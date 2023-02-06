package com.example.mspr_;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button btn_inscription,btn_connexion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        btn_inscription = (Button) findViewById(R.id.btn_inscription);
        btn_connexion = (Button) findViewById(R.id.btn_connexion);



        btn_inscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), InscriptionActivity.class);
                startActivity(intent);
            }
        });

        btn_connexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ConnexionActivity.class);
                startActivity(intent);
            }
        });

    }
}