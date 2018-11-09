package com.example.terry.litiansfirstapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Main2Activity extends AppCompatActivity {

    private TextView textView_info;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        textView_info = findViewById(R.id.info);

        Intent intent = getIntent();
        Bundle data = intent.getBundleExtra("data");
        String info = data.getString("info");
        textView_info.setText(info);

    }


}
