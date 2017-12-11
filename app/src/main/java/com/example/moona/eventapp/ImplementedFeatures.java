package com.example.moona.eventapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by moona on 11.12.2017.
 */

public class ImplementedFeatures extends AppCompatActivity {

    private TextView txtFeatures;
    private Button btnClose;
    private String features = "- ID 1 done\n"
                            + "- ID 2 done\n"
                            + "- ID 3 done\n"
                            + "- ID 4 done\n"
                            + "- ID 5 not done\n"
                            + "- ID 6 done\n"
                            + "- ID 7 done\n";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.implemented_features);

        txtFeatures = findViewById(R.id.txtFeatures);
        btnClose = findViewById(R.id.btnClose);

        txtFeatures.setText(features);


        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
    }
}
