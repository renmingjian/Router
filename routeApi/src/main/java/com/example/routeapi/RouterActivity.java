package com.example.routeapi;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * 统一处理第三方应用跳转到本应用
 * 第三方应用拉起本应用时首先进入到该页面，然后由该页面根据URI去跳转到具体的页面
 * Created by mj on 2021/2/10 16:29
 */
public class RouterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Uri uri = getIntent().getData();
        Routers.getInstance().navigation(this, uri.toString());
        finish();
    }
}
