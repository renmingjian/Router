package com.example.routeapi;

import android.app.Activity;

/**
 * 目标Activity类信息
 * Created by mj on 2021/2/9 20:02
 */
public class RouterInfo {
    // 目标Activity的路径（包括参数）
    private String path;
    // 目标Activity的Class对象
    private Class<? extends Activity> activity;

    public RouterInfo(String path, Class<? extends Activity> activity) {
        this.path = path;
        this.activity = activity;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Class<? extends Activity> getActivity() {
        return activity;
    }

    public void setActivity(Class<? extends Activity> activity) {
        this.activity = activity;
    }
}
