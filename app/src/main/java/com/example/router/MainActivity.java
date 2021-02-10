package com.example.router;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.routeannotation.Route;
import com.example.routeapi.Routers;

@Route("")
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void jumpToModule1(View view) {
        Routers.getInstance().navigation(this, "test://module1?key1=123&key2=456");
    }

    public void jumpToModule2(View view) {
        Routers.getInstance().navigation(this, "test://module2?key1=123&key2=456");
    }
}