package com.example.module1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.routeannotation.Route;
import com.example.routeapi.Routers;

@Route("test://module1?key1=123&key2=456")
public class Module1Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module1);

        String stringExtra = getIntent().getStringExtra(Routers.RAW_URL);
        System.out.println("stringExtra = " + stringExtra);
    }

    public void jumpToModule2(View view) {
        Routers.getInstance().navigation(this, "test://module2?key1=123&key2=456");
    }
}