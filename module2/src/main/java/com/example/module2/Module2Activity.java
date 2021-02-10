package com.example.module2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.routeannotation.Route;
import com.example.routeapi.Routers;

@Route("test://module2?key1=123&key2=456")
public class Module2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module2);

        String stringExtra = getIntent().getStringExtra(Routers.RAW_URL);
        System.out.println("stringExtra = " + stringExtra);
    }

    public void jumpToModule1(View view) {
        Routers.getInstance().navigation(this, "test://module1?key1=123&key2=456");
    }
}