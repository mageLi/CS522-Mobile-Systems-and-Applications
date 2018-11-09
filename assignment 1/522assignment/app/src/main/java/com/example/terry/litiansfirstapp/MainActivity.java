package com.example.terry.litiansfirstapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private EditText editText_info;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText_info = findViewById(R.id.editText_info);
    }
    public void sendClick(View view){
        Intent intent = new Intent(this,Main2Activity.class);

        String info = editText_info.getText().toString();
        Bundle data = new Bundle();
        data.putString("info",info);
        intent.putExtra("data",data);
        startActivity(intent);

    }
}
