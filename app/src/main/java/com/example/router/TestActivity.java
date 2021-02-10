package com.example.router;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.routeannotation.Route;

@Route("test://erye?key1=123&key2=456")
public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

    }
}